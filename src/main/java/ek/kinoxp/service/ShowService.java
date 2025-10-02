package ek.kinoxp.service;

import ek.kinoxp.repository.ShowRepository;
import ek.kinoxp.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowService {
    @Autowired // Injects the ShowRepository
    private ShowRepository showRepository;
}
