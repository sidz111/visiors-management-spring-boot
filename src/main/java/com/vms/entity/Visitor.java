package com.vms.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Visitor  implements Comparable<Visitor> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitorId;
    
    private Integer randomId;

    private String name;
    private String contactNumber;
    private String email;
    private String checkIn;
    private String checkOut;
    private Boolean isCheckOut;
    private String img;
    private String govId;
    
	public Integer getRandomId() {
		return randomId;
	}

	public void setRandomId(Integer randomId) {
		this.randomId = randomId;
	}

	public String getGovId() {
		return govId;
	}

	public void setGovId(String govId) {
		this.govId = govId;
	}

	public String getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}

	public String getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(String checkOut) {
		this.checkOut = checkOut;
	}

	public Boolean getIsCheckOut() {
		return isCheckOut;
	}

	public void setIsCheckOut(Boolean isCheckOut) {
		this.isCheckOut = isCheckOut;
	}

	public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public int compareTo(Visitor o) {
		return this.checkOut.compareTo(o.checkOut);
	}

}
