import { Component, OnInit,ViewContainerRef  } from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';
import {ToastOptions} from 'ng2-toastr';

@Component({
  templateUrl: 'dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  public employeeAttendanceArr = [];
  public employeeAttendanceArrProcessed = [];

  constructor(private dataService: DataService,public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }

  @BlockUI() blockUI: NgBlockUI;
  public notificationOptions = {
    position: ["bottom", "right"],
    timeOut: 5000,
    lastOnBottom: true,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
  }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadAttendance();
    }
  }
  loadAttendance() {
    this.blockUI.start("Loading..");
    let link = ['/dashboard'];
    this.dataService.getPostData(this.dataService.serviceurl + 'getDistinctEmployeesO', null).subscribe(data => {
      this.employeeAttendanceArr = data;
      if (data ) {
        this.employeeAttendanceArr.forEach((item, index) => {
          let obj = { name: "" };
          obj.name = item[1];
          obj[item[2]] = item[0];

          let nameArr = this.employeeAttendanceArrProcessed.map(function (val) { return val.name })
          index = nameArr.indexOf(item[1])
          if (index < 0) {
            this.employeeAttendanceArrProcessed.push(obj);
          } else {
            this.employeeAttendanceArrProcessed[index][item[2]] = item[0];
          }
          setTimeout(() => {
            this.blockUI.stop();
          }, 1500);

        });
      } else {
        this.blockUI.update('No data found ...');
        this.toastr.warning('No data found', 'Sorry!', { showCloseButton: true,dismiss: 'click'});
        setTimeout(() => {
          this.blockUI.stop();
        }, 1500);
      }
      setTimeout(() => {
            this.blockUI.stop();
          }, 1500);
    },
      error => { this.handleError(error, "loadAttendance()"); });
  }
  private handleError(error: any, method: any): Promise<any> {

    this.blockUI.update('An error occurred in DashboardComponent at method ' + method + " " + error);
    this.blockUI.stop();
     this.toastr.error('An error occurred in DashboardComponent at method ' + method + " " + error, 'Failed!', { showCloseButton: true,dismiss: 'click'});
    return Promise.reject(error.message || error);
  }

}
