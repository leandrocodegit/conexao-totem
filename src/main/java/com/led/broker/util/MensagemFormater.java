package com.led.broker.util;

import com.led.broker.model.Conexao;
import com.led.broker.model.Configuracao;
import com.led.broker.model.Mensagem;
import com.led.broker.model.Parametro;
import com.led.broker.model.constantes.Comando;
import com.led.broker.model.constantes.Efeito;
import com.led.broker.model.constantes.StatusConexao;
import com.led.broker.model.constantes.TipoConexao;

import java.util.ArrayList;
import java.util.List;

public class MensagemFormater {

    public static Mensagem formatarMensagem(String mensagemHexa) {
        //TIPO COMANDO(2) + ID(7) + TIPOCONEXAO(2) + PINO(2) + EFEITO(8) + VERSAO(6)
        //1 ETH
        //2 WIFI
        //3 LORA

        var inicio = 0;
        var comando = Comando.fromDescricao(hexToInt(mensagemHexa.substring(inicio, inicio + 2)));

        if (comando.equals(Comando.LORA_PARAMETROS))
            return Mensagem.builder()
                    .id(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 14)))
                    .comando(comando)
                    .mensagem(mensagemHexa)
                    .portas(0)
                    .pino(0)
                    .build();

        return Mensagem.builder()
                .comando(comando)
                .id(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 14)))
                .tipoConexao(TipoConexao.fromDescricao(hexToInt(mensagemHexa.substring(inicio += 14, inicio + 2))))
                .portas(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2)))
                .pino(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2)))
                .statusMCU(StatusConexao.fromDescricao(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2))))
                .efeito(
                        List.of(
                                Efeito.fromDescricao(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2))),
                                Efeito.fromDescricao(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2))),
                                Efeito.fromDescricao(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2))),
                                Efeito.fromDescricao(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2)))
                        ))
                .versao(String.format("%d.%d.%d",
                        hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2)),
                        hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2)),
                        hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2))))
                .mensagem(mensagemHexa)
                .build();

    }

    public static Conexao formatarMensagemLoraABP(String mensagemHexa) {
        var inicio = 16;

        var modo = hexToInt(mensagemHexa.substring(inicio, inicio + 2));

        if(modo == 0)
            return Conexao.builder()
                .modoLora(modo)
                .devEui(mensagemHexa.substring(inicio += 2, inicio + 16))
                .nwkSKey(mensagemHexa.substring(inicio += 16, inicio + 32))
                .appSKey(mensagemHexa.substring(inicio += 32, inicio + 32))
                .devAddr(mensagemHexa.substring(inicio += 32, inicio + 8))
                .txPower(hexToInt(mensagemHexa.substring(inicio += 8, inicio + 2)))
                .dataRate(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2)))
                .adr(hexToInt(mensagemHexa.substring(inicio += 2, inicio + 2)) == 1)
                .build();

        return Conexao.builder()
                .modoLora(hexToInt(mensagemHexa.substring(inicio, inicio + 2)))
                .devEui(mensagemHexa.substring(inicio += 2, inicio + 16))
                .appEui(mensagemHexa.substring(inicio += 16, inicio + 16))
                .appKey(mensagemHexa.substring(inicio += 16, inicio + 32))
                .build();
    }

    public static int[] vetor(String hexa) {
        var tamanho = hexa.length() / 2;
        var params = new int[tamanho];

        List<String> codigos = new ArrayList<>();
        List<Integer> convertidos = new ArrayList<>();
        for (int i = 0; i < hexa.length(); i += 2) {
            codigos.add(hexa.substring(i, i + 2));
        }

        for (int i = 0; i < codigos.size(); i++) {
            params[i] = hexToInt(codigos.get(i));
        }

        return params;
    }

    public static int hexToInt(String hex) {
        var value = Integer.parseInt(hex, 16);
        return value;
    }

}
