package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.ThingDto;
import com.execute.protocol.core.entities.Thing;
import com.execute.protocol.core.mappers.ThingMapper;
import com.execute.protocol.core.repositories.ThingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@AllArgsConstructor
public class ThingServiceImpl implements ThingService {
    private final ThingRepository thingRepository;
    @Override
    public Set<Thing> getThings(Set<Integer> idNumbers){
       return thingRepository.findByIdIn(idNumbers);
    }
}
