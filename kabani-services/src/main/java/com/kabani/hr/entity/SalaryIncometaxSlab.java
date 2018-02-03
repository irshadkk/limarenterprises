package com.kabani.hr.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SalaryIncometaxSlab {
	
	@Id
	private int slabid;
	private float slabamountstart;
	private float slabamountend;
	private float slabpercentage;
	private float slabadditionalamount;
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
	public float getSlabpercentage() {
		return slabpercentage;
	}
	public void setSlabpercentage(float slabpercentage) {
		this.slabpercentage = slabpercentage;
	}
	public float getSlabadditionalamount() {
		return slabadditionalamount;
	}
	public void setSlabadditionalamount(float slabadditionalamount) {
		this.slabadditionalamount = slabadditionalamount;
	}
	
}
