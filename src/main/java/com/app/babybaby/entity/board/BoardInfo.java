package com.app.babybaby.entity.board;

import com.app.babybaby.entity.audit.Period;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @ToString
@Table(name = "TBL_BOARD_INFO")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class BoardInfo extends Period {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    private String boardTitle;
    private String boardContent;

    public BoardInfo(String boardTitle, String boardContent) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
