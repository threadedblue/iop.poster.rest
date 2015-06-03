package iop.poster.rest.core;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="posters")
@NamedQueries({ @NamedQuery(name = "iop.poster.rest.Poster.findAll", query = "SELECT p FROM Poster p") })
public class Poster {
	
	public static final String DATE_FORMAT = "MMM dd, yyyy";
	public static final String TIMEZONE = "edt";

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			DATE_FORMAT);

	@Id
	@SequenceGenerator(name = "pk_sequence", sequenceName = "poster_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pk_sequence")
	long id;

	@Basic
	String email;

	@Column(name="first_name")
	String firstName;

	@Column(name="last_name")
	String lastName;

	@Basic
	String description;
	
	@Basic
	String practitioner;

	@ManyToOne
	@JoinColumn(name = "provider_id")
	Provider provider;

	@Column(name = "from_date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=DATE_FORMAT, timezone=TIMEZONE)
	Date from;

	@Column(name = "to_date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=DATE_FORMAT, timezone=TIMEZONE)
	Date to;

	@Column(name = "foreground")
	String color;

	@Basic
	String background;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "poster_id")
	Set<Like> likes;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "poster_id")
	Set<Beloved> beloved;

	public Poster() {
	}

	@JsonIgnore
	public Color getForegroundAsColor() {
		return getHexAsColor(getColor());
	}

	@JsonIgnore
	public Color getBackgroundAsColor() {
		return getHexAsColor(getBackground());
	}

	@JsonIgnore
	Color getHexAsColor(String hex) {
		return Color.decode(hex);
	}

	@JsonIgnore
	public String getFromAsString() {
		return getDateAsString(getFrom());
	}

	@JsonIgnore
	public String getToAsString() {
		return getDateAsString(getFrom());
	}

	@JsonIgnore
	String getDateAsString(Date date) {
		return sdf.format(date);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public String getName() {
		return getFirstName() + " " + getLastName();
	}

	public String getPractitioner() {
		return practitioner;
	}

	public void setPractitioner(String practitioner) {
		this.practitioner = practitioner;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public Set<Like> getLikes() {
		return likes;
	}

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
	}

	public Set<Beloved> getBeloved() {
		return beloved;
	}

	public void setBeloved(Set<Beloved> beloved) {
		this.beloved = beloved;
	}
}
