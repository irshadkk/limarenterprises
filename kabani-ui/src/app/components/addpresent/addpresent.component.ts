import { Component, TemplateRef, OnInit, ViewContainerRef, ViewChild } from '@angular/core';
import { DataService } from '../../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { NotificationsService } from 'angular4-notify';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';
import { ToastOptions } from 'ng2-toastr';
import { DatePipe } from '@angular/common';

@Component({
  templateUrl: 'addpresent.component.html'
})
export class AddPresentComponent implements OnInit {


  public employeeArr;
  public employeeAttendanceArr;
  public currentItem: any = [];
  public infoModal;
  public statusArr = ["Absent", "Present", "1/2Present"]
  @BlockUI() blockUI: NgBlockUI;
  public notificationOptions = {
    position: ["bottom", "right"],
    timeOut: 5000,
    lastOnBottom: true,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
  }
  constructor(private dataService: DataService, private datePipe: DatePipe, protected notificationsService: NotificationsService, public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadEmployees();
      this.loadExtraAddedAttendance();
    }
  }


  loadEmployees() {
    this.blockUI.start("Loading..");
    this.dataService.getData(this.dataService.serviceurl + 'employee/all').subscribe(data => {
      this.employeeArr = data;
      setTimeout(() => {
        this.blockUI.stop();
      }, 1500);


    },
      (error => { this.handleError(error, "loadEmployees()"); }));
  }
  loadExtraAddedAttendance() {
    this.blockUI.start("Loading..");
    this.dataService.getData(this.dataService.serviceurl + 'manualattendance/all').subscribe(data => {
      this.employeeAttendanceArr = data;
      setTimeout(() => {
        this.blockUI.stop();
      }, 1500);


    },
      (error => { this.handleError(error, "loadEmployees()"); }));
  }

  saveChanges() {
    this.blockUI.start("Saving..");
    this.dataService.getPostData(this.dataService.serviceurl + 'manualattendance/addorupdate', this.currentItem).subscribe(data => {
      if (data == true) {
        this.notificationsService.addInfo('Changes Saved');
        this.toastr.success(' Changes Saved', 'Success!', { duration: 1500, dismiss: 'auto' });
      } else {
        this.notificationsService.addWarning('Changes couldnt be saved');
        this.toastr.error(' Changes Saved', 'Error!', { duration: 1500, dismiss: 'click' });
      }
      setTimeout(() => {
        this.infoModal.hide();
        this.blockUI.stop();
      }, 1500);
    },
      (error => { this.handleError(error, "saveChanges()"); }));

  }
  onEditClick(infoModal, item) {
    this.blockUI.start("Loading..");
     this.infoModal =infoModal;
    this.currentItem = item;
    item.dayOfextraDay = this.datePipe.transform(item.dayOfextraDay, 'yyyy-MM-dd');
    item.dayOfOtherExtraDay = this.datePipe.transform(item.dayOfOtherExtraDay, 'yyyy-MM-dd');

    infoModal.show();
    setTimeout(() => {
      this.blockUI.stop();
    }, 100);
  }
  changeinLeave() {
    this.currentItem.casualLeavesRemaining = parseInt(this.currentItem.totalCasualAlloted) - parseInt(this.currentItem.casualLeavesTaken)
  }

  onAddClick(infoModal) {
    this.currentItem = {};
    this.infoModal =infoModal;

    infoModal.show();
  }
  onChange(event) {
    console.log(event)
    this.currentItem.employeeName = event.employeeName;
    this.currentItem.employeeCode = event.employeeCode;

  }

  private handleError(error: any, method: any) {
    console.error('An error occurred in ViewleavesummaryComponent at method ' + method, +" " + error);
    this.blockUI.stop();
    this.notificationsService.addError('An error occurred in ViewleavesummaryComponent at method ' + method + " " + error);
  }


}
