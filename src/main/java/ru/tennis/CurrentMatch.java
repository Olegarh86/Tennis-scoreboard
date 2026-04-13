package ru.tennis;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CurrentMatch {
    public String uuid;
    public Integer player1Id;
    public Integer player2Id;
    @Builder.Default
    public Map<String, Integer> score = new HashMap<>();
    @Builder.Default
    public Map<String, Integer> game = new HashMap<>();
    @Builder.Default
    public Map<String, Integer> set = new HashMap<>();
    public Boolean endMatch;

    public CurrentMatch(Integer player1Id, Integer player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.score = new HashMap<>();
        this.score.put("1", 0);
        this.score.put("2", 0);
        this.game = new HashMap<>();
        this.game.put("1", 0);
        this.game.put("2", 0);
        this.set = new HashMap<>();
        this.set.put("1", 0);
        this.set.put("2", 0);
        this.uuid = UUID.randomUUID().toString();
        this.endMatch = false;
    }
}
