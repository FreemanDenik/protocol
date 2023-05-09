package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Category;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.account.User;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.repositories.EventRepository;
import com.execute.protocol.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final EventService eventService;
    private final UserRepository userRepository;

}
