package com.fzipp.pay.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
public class Info {
    @Value("${file.networkIP}")
    private String vNetworkIP;

    private static String networkIP;

    @PostConstruct
    public void setNetworkIP(){
        networkIP = this.vNetworkIP;
    }
    public static String getNetworkIP(){
        return networkIP;
    }

}
