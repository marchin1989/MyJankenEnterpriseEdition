package com.example.janken.infrastructure.mysqlrepository;

import com.example.janken.domain.model.janken.Janken;
import com.example.janken.domain.model.janken.JankenRepository;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.infrastructure.dao.JankenDao;
import com.example.janken.infrastructure.dao.JankenDetailDao;
import com.example.janken.registry.ServiceLocator;
import lombok.val;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JankenMySQLRepository implements JankenRepository {

    private final JankenDao jankenDao = ServiceLocator.resolve(JankenDao.class);
    private final JankenDetailDao jankenDetailDao = ServiceLocator.resolve(JankenDetailDao.class);

    @Override
    public List<Janken> findAllOrderById(Transaction tx) {
        val jankensWithoutJankenDetail = jankenDao.findAllOrderById(tx);
        val jankenDetails = jankenDetailDao.findAllOrderById(tx);

        return jankensWithoutJankenDetail.stream()
                .map(janken -> {
                    val targetDetails = jankenDetails.stream()
                            .filter(jankenDetail -> jankenDetail.getJankenId().equals(janken.getId()))
                            .collect(Collectors.toList());

                    return new Janken(
                            janken.getId(),
                            janken.getPlayedAt(),
                            targetDetails.get(0),
                            targetDetails.get(1));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Janken> findById(Transaction tx, long id) {
        val jankenWithoutJankenDetail = jankenDao.findById(tx, id);
        return jankenWithoutJankenDetail.stream()
                .findFirst()
                .map(janken -> {
                    val jankenDetails = jankenDetailDao.findByJankenIdOrderById(tx, id);
                    return new Janken(
                            janken.getId(),
                            janken.getPlayedAt(),
                            jankenDetails.get(0),
                            jankenDetails.get(1));
                });
    }

    @Override
    public long count(Transaction tx) {
        return jankenDao.count(tx);
    }

    @Override
    public Janken save(Transaction tx, Janken janken) {
        val jankenWithId = jankenDao.insert(tx, janken);
        jankenWithId.details();
        val jankenDetailsWithId = jankenDetailDao.insertAll(tx, jankenWithId.details());
        return new Janken(
                jankenWithId.getId(),
                jankenWithId.getPlayedAt(),
                jankenDetailsWithId.get(0),
                jankenDetailsWithId.get(1)
        );
    }
}
