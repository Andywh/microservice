package com.joy.user.thrift;

import com.joy.thrift.message.MessageService;
import com.joy.thrift.user.UserService;
import com.joy.user.enums.ServiceTypeEnum;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by SongLiang on 2019-08-09
 */
@Component
public class ServiceProvider {

    @Value("${thrift.user.ip}")
    private String userServerIp;

    @Value("${thrift.user.port}")
    private int userServerPort;

    @Value("${thrift.message.ip}")
    private String messageServerIp;

    @Value("${thrift.message.port}")
    private int messageServerPort;

    public UserService.Client getUserService() {
        return getService(userServerIp, userServerPort, ServiceTypeEnum.USER);
    }

    public MessageService.Client getMessageService() {
        return getService(messageServerIp, messageServerPort, ServiceTypeEnum.MESSAGE);
    }

    public <T> T getService(String ip, int port, ServiceTypeEnum serviceTypeEnum) {
        TSocket socket = new TSocket(ip, port, 3000);
        TTransport transport = new TFramedTransport(socket);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        TProtocol protocol = new TBinaryProtocol(transport);

        TServiceClient client = null;
        switch (serviceTypeEnum) {
            case USER:
                client = new UserService.Client(protocol);
                break;
            case MESSAGE:
                client = new MessageService.Client(protocol);
                break;
        }

        return (T)client;
    }



}
