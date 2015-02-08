package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import play.db.ebean.Model;

@Entity
public class Entry extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	
	@NotNull
	@Size(min = 1, max = 255)
	public String title;
	
	@NotNull
	public String message;
	
	private static final long serialVersionUID = 3300403414741352368L;

	public static Finder<Integer, Entry> find = new Finder<Integer, Entry>(Integer.class, Entry.class);
	
	public Entry(String title, String message) {
		this.title = title;
		this.message = message;
	}

	public static boolean isEmpty(){
		return find.findRowCount() == 0;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
