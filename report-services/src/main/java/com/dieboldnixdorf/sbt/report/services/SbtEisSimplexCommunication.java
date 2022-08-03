package com.dieboldnixdorf.sbt.report.services;

import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.dieboldnixdorf.txm.core.businessmodel.container.Request;
import com.dieboldnixdorf.txm.core.businessmodel.container.Response;
import com.dieboldnixdorf.txm.core.businessmodel.json.JsonMapper;
import com.myproclassic.server.eisconnector.IPCEEISConnection;
import com.myproclassic.server.eisconnector.IPCEEISConnectionFactory;
import com.myproclassic.server.eisconnector.PCEEISConnectorData;
import com.myproclassic.server.eisconnector.impl.PCEEISConnector;
import com.myproclassic.server.eisconnector.utils.CommTrace;

@ApplicationScoped
public class SbtEisSimplexCommunication {

    public static final String EIS_ID = "SBT_EIS_SIMPLEX";

    private static JsonMapper<Request>  requestMapper  = new JsonMapper<>(Request.class);
    private static JsonMapper<Response> responseMapper = new JsonMapper<>(Response.class);

    @Inject
    private CommTrace commTrace;


    public Response sendAndReceive(Request txmReq) throws Exception {

        String  req    = requestMapper.toJsonString(txmReq);
        byte[]  msg    = req.getBytes(StandardCharsets.UTF_8);

        PCEEISConnectorData eisConnectorData =
                new PCEEISConnectorData(txmReq.getHeader().getClientId(), EIS_ID, msg);

        PCEEISConnector connector = PCEEISConnector.getConnector(EIS_ID);
        IPCEEISConnectionFactory cf = connector.getConnectionFactory();

        try (IPCEEISConnection c = cf.getConnection()) {

            commTrace.commTraceSend(eisConnectorData, cf.getMetaData());

            eisConnectorData = c.sendAndReceive(eisConnectorData);

            commTrace.commTraceReceive(eisConnectorData, cf.getMetaData());
        }


        String   rsp    = new String(eisConnectorData.getMessage(), StandardCharsets.UTF_8);
        Response txmRsp = responseMapper.fromJson(rsp);

        return txmRsp;
    }

}