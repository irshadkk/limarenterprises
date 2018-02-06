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

  constructor(private dataService: DataService, private datePipe: DatePipe) { }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadHolidays();
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
