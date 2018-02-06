import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';


@Component({
  templateUrl: 'viewattendance.component.html'
})
export class ViewAttendanceComponent implements OnInit {
  public employeeAttendanceArr;
  public currentItem = [];
  public statusArr = ["Absent", "Present", "1/2Present"];
  @BlockUI() blockUI: NgBlockUI;

  constructor(private dataService: DataService) { }

  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadAttendance();
    }
  }


  loadAttendance() {
    this.blockUI.start("Loading..");
    this.dataService.getPostData(this.dataService.serviceurl + 'getAllAttandance', null).subscribe(data => {
      this.employeeAttendanceArr = data;
      setTimeout(() => {
        this.blockUI.stop();
      }, 1500);


    });
  }

  saveChanges() {
    console.log(JSON.stringify(this.currentItem))
    this.dataService.getPostData(this.dataService.serviceurl + 'updateUserAttandance', this.currentItem).subscribe(data => {
      alert(data)



    });

  }
  onEditClick(infoModal, item) {
    this.currentItem = item;
    console.log(JSON.stringify(this.currentItem))
    infoModal.show()

  }


}
