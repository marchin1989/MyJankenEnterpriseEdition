package com.example.janken.infrastructure.mysqlrepository;

import com.example.janken.domain.model.janken.Hand;
import com.example.janken.domain.model.janken.Janken;
import com.example.janken.domain.model.janken.JankenDetail;
import com.example.janken.domain.model.janken.JankenRepository;
import com.example.janken.infrastructure.jooq.generated.janken.tables.JANKENS_TABLE;
import com.example.janken.infrastructure.jooq.generated.janken.tables.JANKEN_DETAILS_TABLE;
import com.example.janken.infrastructure.jooq.generated.janken.tables.records.JankensRecord;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;
import org.jooq.Record7;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.types.UInteger;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class JankenMySQLRepository implements JankenRepository {
    private static final JANKENS_TABLE J = JANKENS_TABLE.JANKENS.as("J");
    private static final JANKEN_DETAILS_TABLE JD = JANKEN_DETAILS_TABLE.JANKEN_DETAILS.as("JD");

    private DSLContext db;

    @Override
    public List<Janken> findAllOrderByPlayedAt() {
        val groups = selectFrom()
                .orderBy(J.PLAYED_AT)
                .fetchGroups(J);

        return groups2Models(groups);
    }

    @Override
    public Optional<Janken> findById(String id) {
        val groups = selectFrom()
                .where(J.ID.eq(id))
                .fetchGroups(J);
        return groups2Models(groups).stream().findFirst();
    }

    @Override
    public long count() {
        return db.fetchCount(J);
    }

    @Override
    public void save(Janken janken) {
        val jankenRecord = new JankensRecord(janken.getId(), janken.getPlayedAt());
        db.insertInto(J)
                .set(jankenRecord)
                .execute();

        val insertVluesStep = db.insertInto(JD, JD.ID, JD.JANKEN_ID, JD.PLAYER_ID, JD.HAND, JD.RESULT);

        janken.details().forEach(jd ->
                insertVluesStep.values(
                        jd.getId(),
                        jd.getJankenId(),
                        jd.getPlayerId(),
                        UInteger.valueOf(jd.getHand().getValue()),
                        UInteger.valueOf(jd.getResult().getValue())));
        insertVluesStep.execute();
    }

    private List<Janken> groups2Models(Map<JankensRecord, Result<Record7<String, LocalDateTime, String, String, String, UInteger, UInteger>>> groups) {
        return groups.entrySet().stream()
                .map(this::group2Model)
                .collect(Collectors.toList());
    }

    private Janken group2Model(Map.Entry<JankensRecord, Result<Record7<String, LocalDateTime, String, String, String, UInteger, UInteger>>> group) {
        val j = group.getKey();
        val jankenDetails = group.getValue().stream()
                .map(jd -> new JankenDetail(
                        jd.get(JD.ID),
                        jd.get(JD.JANKEN_ID),
                        jd.get(JD.PLAYER_ID),
                        Hand.of(jd.get(JD.HAND).intValue()),
                        com.example.janken.domain.model.janken.Result.of(jd.get(JD.RESULT).intValue())))
                .sorted(Comparator.comparing(JankenDetail::getId))
                .collect(Collectors.toList());
        return new Janken(
                j.getId(),
                j.getPlayedAt(),
                jankenDetails.get(0),
                jankenDetails.get(1));
    }

    private SelectJoinStep<Record7<String, LocalDateTime, String, String, String, UInteger, UInteger>> selectFrom() {
        return db.select(J.ID, J.PLAYED_AT, JD.ID, JD.JANKEN_ID, JD.PLAYER_ID, JD.HAND, JD.RESULT)
                .from(J)
                .innerJoin(JD)
                .on(J.ID.eq(JD.JANKEN_ID));
    }
}
