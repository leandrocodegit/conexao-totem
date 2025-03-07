package com.led.broker.controller.response;

import com.led.broker.model.Log;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashboardResponse {

    private long usuariosAtivos;
    private long usuariosInativos;
    private DispositivoDashResponse dispositivos;
    private List<DispositivoPorCorResponse> agendas;
    private List<DispositivoPorCorResponse> agendasExecucao;
    private List<DispositivoPorCorResponse> cores;
    private List<Log> logs;
    private List<DispositivoPorCorResponse> logsConexao;
}