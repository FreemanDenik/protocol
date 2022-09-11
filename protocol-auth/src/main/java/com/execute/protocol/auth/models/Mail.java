package com.execute.protocol.auth.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;


/**
 * Bean который подтягивает mail данные из properties
 */
@Component
@ConfigurationProperties(prefix = "mail")
@ConfigurationPropertiesScan("com.i.execute.game.auth")
public class Mail extends AbstractProvider{
}