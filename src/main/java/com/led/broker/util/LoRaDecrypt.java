package com.led.broker.util;

import com.led.broker.model.Mensagem;
import com.led.broker.model.constantes.StatusConexao;
import com.led.broker.model.constantes.TipoConexao;
import com.led.broker.repository.ConexaoRepository;
import com.led.broker.repository.DispositivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class LoRaDecrypt {

    private final ConexaoRepository conexaoRepository;

    public String decript(Mensagem mensagem) {
        try {

            var conexao = conexaoRepository.findByDevEui(mensagem.getMeta().getDevice());

            if (conexao.isPresent()) {
                var atualizarConexao = false;
                if (mensagem.getType() != null && mensagem.getType().equals("uplink")) {
                    conexao.get().setStatus(StatusConexao.Online);
                    conexao.get().setTipoConexao(TipoConexao.LORA);
                    atualizarConexao = true;
                } else if (mensagem.getType() != null && mensagem.getType().equals("location") && conexao.get().getLatitude() == null && mensagem.getParams() != null) {
                    conexao.get().setLatitude(mensagem.getParams().getSolutions().get(0).getLat());
                    conexao.get().setLongitude(mensagem.getParams().getSolutions().get(0).getLng());
                    atualizarConexao = true;
                }
                if (mensagem.getParams() != null && mensagem.getParams().getHardware() != null) {
                    conexao.get().setSnr(mensagem.getParams().getHardware().getSnr());
                    conexao.get().setRssi(mensagem.getParams().getHardware().getRssi());
                    atualizarConexao = true;
                }
                if (atualizarConexao)
                    conexaoRepository.save(conexao.get());
            }

            if (mensagem.getParams().getEncrypted_payload() == null)
                return null;


            byte[] decodedBytes = Base64.getDecoder().decode(mensagem.getParams().getPayload());
            String decodedString = new String(decodedBytes);


            return decodedString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
