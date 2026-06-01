package ru.tennis.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "players") // можно задать индекс через аннотацию, чтобы у него было понятное имя — @Table(name = "players", indexes = @Index(...))
public class Player {

    // Более уместным было бы разместить класс в пакете 'entity'.
        // (см. файл "model-types.md" в этом же пакете)

    // Поле `id` имеет тип `int`, который имеет максимальное значение `~2.1` миллиарда.
        // Хотя `Integer` соответствует ТЗ, максимальное значение `Integer` может быть исчерпано в системах с большим количеством записей.
        // Общепринятой и хорошей практикой для первичных ключей является использование типа `Long`.
        // Лучше заменить тип поля `id` на `Long`.

    // TODO: Для корректного и безопасного создания новых, ещё не сохранённых в БД игроков, стоит создать и использовать конструктор со всеми полями, кроме ID.

    // TODO: Модификаторы public у всех полей нарушают инкапсуляцию и позволяют изменить их даже при отсутствии сеттеров.

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer id;

    @Getter
    @Column(name = "name", unique = true, nullable = false) // Можно добавить length = 30, чтобы задать ограничения на уровне БД
    public String name;

    public Player(String name) {
        this.name = name;
    }
}
