package fr.cinema.domain.commands;

import jakarta.transaction.Transactional;

public interface DomainCommand {
    @Transactional
@org.springframework.transaction.annotation.Transactional
    void execute();
}
