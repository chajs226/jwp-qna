package qna.domain;

import lombok.Builder;
import org.hibernate.type.LocalDateType;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.common.BaseTimeEntity;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "answer")
@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToOne
    @JoinColumn(name = "writer_id")
    private User user;

    protected Answer() {}

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        //this.writerId = writer.getId();
        //this.questionId = question.getId();
        this.user = writer;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        //return this.writerId.equals(writer.getId());
        return this.user.getId().equals(writer.getId());
    }

    public void toQuestion(Question question) {
        this.question.setId(question.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWriterId() {
        return user.getId();
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + user.getId() +
                ", questionId=" + question.getId() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
