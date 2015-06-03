package iop.poster.rest.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="likes")
public class Like extends BaseEntity {
	
	public Like() {
		super();
	}

	public Like(String like) {
		super();
		this.like = like;
	}

	public Like(Long id, String like) {
		super(id);
		this.like = like;
	}

	@Column(name="like_")
	private String like;

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

}
