package com.led.broker.service;

import com.led.broker.model.*;

import static com.led.broker.model.constantes.Comando.*;
import static com.led.broker.model.constantes.StatusConexao.*;
import static com.led.broker.model.constantes.Topico.*;
import static com.led.broker.model.constantes.ModoOperacao.*;
import static com.led.broker.model.constantes.TipoCor.*;

import com.led.broker.model.constantes.*;
import com.led.broker.repository.*;
import com.led.broker.util.MensagemFormater;
import com.led.broker.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DispositivoService {

    private static final Logger logger = LoggerFactory.getLogger(DispositivoService.class);
    @Value("${quantidade-clientes}")
    private int quantidadeClientes;
    private final DispositivoRepository dispositivoRepository;
    private final LogRepository logRepository;
    private final DashboardService dashboardService;
    private final ComandoService comandoService;
    private final OperacaoRepository operacaoRepository;
    private final ConexaoRepository conexaoRepository;
    private final MongoTemplate mongoTemplate;
    private final MqttService mqttService;
    private final CorRepository corRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    public static Map<String, UUID> clientes = new HashMap<>();


    public void salvarDispositivoComoOffline(List<Conexao> conexoes) {
        if (conexoes != null && !conexoes.isEmpty()) {

            conexoes.forEach(conexao -> conexao.setStatus(Offline));

            conexaoRepository.saveAll(conexoes);

            logRepository.save(Log.builder()
                    .data(LocalDateTime.now())
                    .usuario("Enviado pelo sistema")
                    .mensagem("Dispositivos offline")
                    .cor(null)
                    .comando(OFFLINE)
                    .descricao(String.format(OFFLINE.value, "grupo"))
                    .id("")
                    .tipoLog(TipoLog.CONEXAO)
                    .build());
            logger.warn("Erro ao capturar id");
        }
    }


    public void atualizarDispositivo(Mensagem mensagem) {
        Optional<Dispositivo> dispositivoOptional = Optional.empty();
        if (!Thread.currentThread().isInterrupted()) {
            dispositivoOptional = dispositivoRepository.findByIdAndAtivo(mensagem.getId(), true);
        }
        logger.warn("Comando recebido: " + mensagem.getComando());
        if (dispositivoOptional.isPresent() && mensagem.getId() > 1000) {
            Dispositivo dispositivo = dispositivoOptional.get();

            if (mensagem.getComando().equals(LORA_PARAMETROS)) {

                var conexaoFormater = MensagemFormater.formatarMensagemLoraABP(mensagem.getMensagem());

                dispositivo.getConexao().setModoLora(conexaoFormater.getModoLora());
                dispositivo.getConexao().setDevEui(conexaoFormater.getDevEui());
                dispositivo.getConexao().setTxPower(conexaoFormater.getTxPower() == null ? 0 : conexaoFormater.getTxPower());
                dispositivo.getConexao().setDataRate(conexaoFormater.getDataRate() == null ? 0 : conexaoFormater.getDataRate());
                dispositivo.getConexao().setAdr(conexaoFormater.getAdr());
                dispositivo.getConexao().setSnr(conexaoFormater.getSnr() == null ? 0 : conexaoFormater.getSnr());
                dispositivo.getConexao().setRssi(conexaoFormater.getRssi() == null ? 0 : conexaoFormater.getRssi());

                if (conexaoFormater.getModoLora() == 0) {
                    dispositivo.getConexao().setNwkSKey(conexaoFormater.getNwkSKey());
                    dispositivo.getConexao().setAppSKey(conexaoFormater.getAppSKey());
                    dispositivo.getConexao().setDevAddr(conexaoFormater.getDevAddr());
                } else {
                    dispositivo.getConexao().setDevEui(conexaoFormater.getDevEui());
                    dispositivo.getConexao().setAppEui(conexaoFormater.getAppEui());
                    dispositivo.getConexao().setAppKey(conexaoFormater.getAppKey());
                }

                if (dispositivo.getConexao().getTempoAtividade() == null)
                    dispositivo.getConexao().setTempoAtividade(3);
                if (!Thread.currentThread().isInterrupted()) {
                    conexaoRepository.save(dispositivo.getConexao());
                }
            } else {


                boolean gerarLog = mensagem.getComando().equals(ONLINE) && dispositivo.getConexao().getStatus().equals(Offline);
                boolean atualizarDashboard = mensagem.getComando().equals(CONFIGURACAO) && dispositivo.getConexao().getStatus().equals(Offline);

                if (dispositivo.getConexao() == null) {
                    dispositivo.setConexao(Conexao.builder()
                            .id(String.valueOf(dispositivo.getId()))
                            .status(Online)
                            .autoJoin(false).build());
                }
                if (dispositivo.getCor() == null && !Thread.currentThread().isInterrupted()) {
                    var cor = corRepository.save(corRepository.findById(String.valueOf(mensagem.getId())).orElse(Cor.padrao(String.valueOf(mensagem.getId()))));
                    dispositivo.setCor(cor);
                }

                if (dispositivo.getCor().isExclusiva())
                    dispositivo.getCor().setNome(String.valueOf(dispositivo.getId()));
                dispositivo.getConexao().setUltimaAtualizacao(LocalDateTime.now().atZone(ZoneOffset.UTC).toLocalDateTime());
                dispositivo.getConexao().setStatus(Online);
                dispositivo.getConexao().setStatusMCU(mensagem.getStatusMCU());
                dispositivo.getConexao().setTipoConexao(mensagem.getTipoConexao());
                if (dispositivo.getConexao().getTempoAtividade() == null)
                    dispositivo.getConexao().setTempoAtividade(3);
                if (!Thread.currentThread().isInterrupted()) {
                    conexaoRepository.save(dispositivo.getConexao());
                }
                logger.warn("Atualizado conexão:  " + dispositivo.getId() + " : " + dispositivo.getConexao().getStatus());
                dispositivo.setComando(mensagem.getComando());
                dispositivo.setVersao(mensagem.getVersao());
                dispositivo.setBrokerId(mensagem.getBrockerId());
                if (!Thread.currentThread().isInterrupted()) {
                    dispositivoRepository.save(dispositivo);
                }
                logger.warn("Atualizado dispositivo:  " + dispositivo.getId());

                if (gerarLog || atualizarDashboard && dispositivo.getCliente() != null)
                    clientes.put(dispositivo.getCliente().getId().toString(), dispositivo.getCliente().getId());
                if (gerarLog && !Thread.currentThread().isInterrupted()) {
                    logRepository.save(Log.builder()
                            .cliente(dispositivo.getCliente())
                            .data(LocalDateTime.now())
                            .usuario("Enviado pelo dispositivo")
                            .mensagem("mensagem.getId()")
                            .cor(dispositivo.getCor())
                            .comando(mensagem.getComando())
                            .descricao(mensagem.getComando().equals(ONLINE) ? String.format(mensagem.getComando().value, mensagem.getId()) : mensagem.getComando().value)
                            .id(String.valueOf(dispositivo.getId()))
                            .tipoLog(TipoLog.CONEXAO)
                            .build());
                    logger.warn("Criado log de tarefa");
                }
                if ((mensagem.getComando().equals(Comando.OCORRENCIA) || mensagem.getComando().equals(BOTAO_ACIONADO)) && dispositivo.getOperacao().getCorVibracao() != null) {
                    if (mensagem.getComando().equals(Comando.OCORRENCIA))
                        dispositivo.getOperacao().setModoOperacao(ModoOperacao.OCORRENCIA);
                    else if (mensagem.getComando().equals(BOTAO_ACIONADO)) {
                        dispositivo.getOperacao().setModoOperacao(BOTAO);
                    }
                    if (!Thread.currentThread().isInterrupted()) {
                        operacaoRepository.save(dispositivo.getOperacao());
                        comandoService.temporizarInterno(dispositivo.getOperacao().getCorVibracao().getId(), dispositivo.getId());
                    }
                } else {
                    sincronizar(dispositivo, mensagem);
                }
            }
        } else {
            if (dispositivoRepository.countByAtivo(true) < quantidadeClientes && dispositivoRepository.countByAtivo(false) < quantidadeClientes + 100) {

                if (mensagem.getId() > 1000) {

                    var cor = corRepository.save(corRepository.findById(String.valueOf(mensagem.getId())).orElse(Cor.padrao(String.valueOf(mensagem.getId()))));
                    Dispositivo dispositivo = dispositivoRepository.save(
                            Dispositivo.builder()
                                    .conexao(Conexao.builder()
                                            .id(String.valueOf(mensagem.getId()))
                                            .status(Online)
                                            .ultimaAtualizacao(LocalDateTime.now())
                                            .statusMCU(mensagem.getStatusMCU())
                                            .tipoConexao(mensagem.getTipoConexao())
                                            .fracionarMensagem(false)
                                            .autoJoin(false)
                                            .build())
                                    .id(mensagem.getId())
                                    .topico(mensagem.getId())
                                    .versao(mensagem.getVersao())
                                    .ignorarAgenda(false)
                                    .sensibilidadeVibracao(0.0F)
                                    .cor(cor)
                                    .operacao(Operacao.builder()
                                            .id(String.valueOf(mensagem.getId()))
                                            .modoOperacao(DISPOSITIVO)
                                            .time(LocalDateTime.now())
                                            .build())
                                    .ativo(false)
                                    .permiteComando(true)
                                    .nome(String.valueOf(mensagem.getId()))
                                    .comando(ONLINE)
                                    .build());
                    logger.warn("Novo dispositivo adicionado " + dispositivo.getId());
                    conexaoRepository.save(dispositivo.getConexao());
                    operacaoRepository.save(dispositivo.getOperacao());
                } else {
                    var id = sequenceGeneratorService.getNextSequence("deviceId");
                    comandoService.sincronizarId(id, mensagem.getId());
                }
            }
        }
    }

    public void sincronizar(Mensagem mensagem) {
        logger.warn("Executando sincronizar: " + mensagem.getId());
        mensagem.setComando(CONFIGURACAO);
        dispositivoRepository.findByIdAndAtivo(mensagem.getId(), true).ifPresent(device -> sincronizar(device, mensagem));
    }

    public void sincronizar(Dispositivo dispositivo, Mensagem mensagem) {
        var cor = repararCor(dispositivo);
        logger.info("Nova mensagem " + mensagem.getComando().value);
        if (cor == null) {
            logger.warn("Sem cor definina");
        } else if (Stream.of(CONCLUIDO).anyMatch(cmd -> cmd.equals(mensagem.getComando()))) {
            comandoService.sincronizar(dispositivo.getId());
            logger.warn("Tarefa de configuração executada");
        } else if (mensagem.getComando().equals(ONLINE) || mensagem.getComando().equals(CONFIGURACAO)) {
            var efeitoRemoto = mensagem.getEfeito().stream().filter(efeito -> !efeito.equals(Efeito.SEM_EFEITO)).toList();
            if (cor.getParametros() != null) {
                var efeitosDispositivo = cor.getParametros().stream().map(Parametro::getEfeito).toList();
                if (!efeitoRemoto.containsAll(efeitosDispositivo)) {
                    logger.warn("Reparação de efeito de " + cor.getParametros().get(0).getEfeito() + " para " + mensagem.getEfeito());
                    comandoService.sincronizar(dispositivo.getId());
                }
            }
        }
    }

    private Cor repararCor(Dispositivo dispositivo) {

        logger.warn("Recuperando cor");
        if (dispositivo.getOperacao().getModoOperacao().equals(DISPOSITIVO)) {
            logger.info("Tipo: " + dispositivo.getOperacao().getModoOperacao());
            return dispositivo.getCor();
        }

        if (Stream.of(TEMPORIZADOR, BOTAO).anyMatch(modo -> dispositivo.getOperacao().getModoOperacao().equals(modo) && dispositivo.isPermiteComando())) {
            if (TimeUtil.isTime(dispositivo)) {
                if (dispositivo.getOperacao().getCorTemporizador() != null) {
                    logger.info("Tipo: " + dispositivo.getOperacao().getModoOperacao());
                    return dispositivo.getOperacao().getCorTemporizador();
                }
            }
        }

        if (Boolean.FALSE.equals(dispositivo.isIgnorarAgenda()) && dispositivo.getOperacao().getModoOperacao().equals(AGENDA)) {
            Agenda agenda = dispositivo.getOperacao().getAgenda();
            if (agenda != null && agenda.getCor() != null && agenda.isAtivo() && agenda.getDispositivos().contains(dispositivo.getId())) {
                MonthDay inicio = MonthDay.from(agenda.getInicio());
                MonthDay fim = MonthDay.from(agenda.getTermino());

                MonthDay hoje = MonthDay.from(LocalDate.now());

                boolean isBetween = false;
                if (inicio.isBefore(fim) || inicio.equals(fim)) {
                    isBetween = (hoje.equals(inicio) || hoje.isAfter(inicio)) &&
                            (hoje.equals(fim) || hoje.isBefore(fim));
                }
                if (isBetween) {
                    logger.info("Tipo: " + dispositivo.getOperacao().getModoOperacao());
                    return agenda.getCor();
                }
            }
        }

        if (!dispositivo.getOperacao().getModoOperacao().equals(DISPOSITIVO)) {
            dispositivo.getOperacao().setModoOperacao(DISPOSITIVO);
            operacaoRepository.save(dispositivo.getOperacao());
            logger.warn("Reset modo operação: " + dispositivo.getOperacao().getModoOperacao());
        }

        logger.info("Tipo: " + dispositivo.getOperacao().getModoOperacao());
        return dispositivo.getCor();
    }

    public List<Conexao> dispositivosQueFicaramOffilne() {

        Date cincoMinutosAtras = Date.from(Instant.now().minusSeconds(5 * 60));
        Criteria criteria = Criteria.where("ultimaAtualizacao").lt(cincoMinutosAtras).and("status").is("Online");
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Conexao.class);
    }
}
