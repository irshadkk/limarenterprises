import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../../data.service';
import { DatePipe } from '@angular/common';

@Component({
  templateUrl: 'addholidays.component.html'
})
export class AddholidaysComponent implements OnInit {
  public date: ''
  public comment: ''
  public holidayCurrentObj = { typeOfHoliday: '', nameOfHoliday: '', descOfHoliday: '', dateOfHoliday: '' }
  public holidayArr = [];
  public monthSelectArr = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  public yearSelectArr = ["2017", "2018", "2019", "2020", "2020", "2021",
    "2022", "2023", "2024", "2025", "2026", "2027"];
  public month = this.monthSelectArr[0];
  public year = this.yearSelectArr[0];

  constructor(private dataService: DataService, private datePipe: DatePipe) { }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadHolidaysByYearMonth();
    }
  }

  public brandPrimary = '#20a8d8';
  public brandSuccess = '#4dbd74';
  public brandInfo = '#63c2de';
  public brandWarning = '#f8cb00';
  public brandDanger = '#f86c6b';


  loadHolidays() {
    this.dataService.getPostData(this.dataService.serviceurl + 'holiday/all', null).subscribe(data => {
      this.holidayArr = data;
    });
  }
   objChanged() {
    this.holidayArr = [];
    this.loadHolidaysByYearMonth();
  }
  loadHolidaysByYearMonth() {//(this.year, )
    this.dataService.getData(this.dataService.serviceurl + 'holiday/all/'+(this.monthSelectArr.indexOf(this.month.toString())+1)+'/'+this.year).subscribe(data => {
      this.holidayArr = data;
    });
    //  this.dataService.getData(this.dataService.serviceurl + 'holiday/all/12/2017').subscribe(data => {
    //   this.holidayArr = data;
    // });
  }
  onEditClick(infoModal, item) {
    if (item) {
      console.log(JSON.stringify(item))

      item.dateOfHoliday = this.datePipe.transform(item.dateOfHoliday, 'yyyy-MM-dd');
      console.log(JSON.stringify(item.dateOfHoliday))
      this.holidayCurrentObj = item;
    }
    else {
      this.holidayCurrentObj = { typeOfHoliday: '', nameOfHoliday: '', descOfHoliday: '', dateOfHoliday: '' };
    }
    infoModal.show()
  }
  addOrEditHolidays(infoModal) {
    this.dataService.getPostData(this.dataService.serviceurl + 'holiday/addorupdate', this.holidayCurrentObj).subscribe(data => {
      if (data) {
        this.loadHolidays()
        infoModal.hide();
      }
    });
  }
  deleteHolidays(item) {
    this.dataService.getPostData(this.dataService.serviceurl + 'holiday/delete', item).subscribe(data => {
      if (data) {
        this.loadHolidays()

      }
    });
  }


}
