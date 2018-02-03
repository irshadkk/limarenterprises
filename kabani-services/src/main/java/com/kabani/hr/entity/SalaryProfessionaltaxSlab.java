package com.kabani.hr.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SalaryProfessionaltaxSlab {
	
	@Id
	private int slabid;
	private float slabamountstart;
	private float slabamountend; 
	private float slabamount;
	public int getSlabid() {
		return slabid;
	}
	public void setSlabid(int slabid) {
		this.slabid = slabid;
	}
	public float getSlabamountstart() {
		return slabamountstart;
	}
	public void setSlabamountstart(float slabamountstart) {
		this.slabamountstart = slabamountstart;
	}
	public float getSlabamountend() {
		return slabamountend;
	}
	public void setSlabamountend(float slabamountend) {
		this.slabamountend = slabamountend;
	}
	public float getSlabamount() {
		return slabamount;
	}
	public void setSlabamount(float slabamount) {
		this.slabamount = slabamount;
	}
	 
	
}
