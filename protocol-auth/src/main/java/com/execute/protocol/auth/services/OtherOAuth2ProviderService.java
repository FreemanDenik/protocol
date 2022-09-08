package com.execute.protocol.auth.services;

import com.execute.protocol.auth.enums.EnumProviders;
import com.execute.protocol.auth.models.AbstractProvider;
import com.execute.protocol.auth.models.Vkontakte;
import com.execute.protocol.auth.models.Yandex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для конвертирования переменных провайдеров
 * (yandex, vkontakte и т.д., у них есть разница в названиях, на пример: yandex - default_email, а у vkontakte - email)
 * к единым поля, так чтобы можно было просто обращаться к указанному провайдеру и к единым названия
 */
@Service
public class OtherOAuth2ProviderService {
    // карта, которая группирует провайдеров
    private final Map<EnumProviders, AbstractProvider> mapAttributes;
    // заполнение карты данными провайдеров
    @Autowired
    private OtherOAuth2ProviderService(Yandex yandex, Vkontakte vkontakte) {

        mapAttributes = new HashMap<>();
        mapAttributes.put(EnumProviders.YANDEX, yandex);
        mapAttributes.put(EnumProviders.VKONTAKTE, vkontakte);
    }

    // заполнение карты данными провайдеров
    // PostConstruct сыграл важную роль, без него класс получает данные из properties,
    // но при передаче они бывают null
//    @PostConstruct
//    private void bind() {
//
//        mapAttributes = new HashMap<>();
//        mapAttributes.put(EnumProviders.YANDEX, yandex);
//        mapAttributes.put(EnumProviders.VKONTAKTE, vkontakte);
//    }
    // Получение полей указанного провайдера
    public AbstractProvider getAttributes(EnumProviders provider){
        return mapAttributes.get(provider);
    }
}
