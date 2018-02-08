import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { NotificationsService } from 'angular4-notify';

@Component({
  templateUrl: 'viewleavesummary.component.html'
})
export class ViewleavesummaryComponent implements OnInit {


  public employeeArr;
  public currentItem :any= [];
  public currentSalaryItem = [];
  public currentLeaveItem = [];
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
  constructor(private dataService: DataService, protected notificationsService: NotificationsService) { }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadEmployees();
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

  saveChanges() {
    this.blockUI.start("Saving..");
    this.dataService.getPostData(this.dataService.serviceurl + 'employee/addorupdate', this.currentItem).subscribe(data => {
      if(data==true){
        this.notificationsService.addInfo('Changes Saved');
      }else{
        this.notificationsService.addWarning('Changes couldnt be saved');
      }
      setTimeout(() => {
        this.blockUI.stop();
      }, 1500);
    },
      (error => { this.handleError(error, "saveChanges()"); }));

  }
  onEditClick(infoModal, item) {
    this.blockUI.start("Loading..");
    this.currentItem = item;

    infoModal.show();
    setTimeout(() => {
      this.blockUI.stop();
    }, 100);
  }
  changeinLeave(){
    this.currentItem.casualLeavesRemaining=parseInt(this.currentItem.totalCasualAlloted)-parseInt( this.currentItem.casualLeavesTaken)
  }

  onResetClick() {
    this.employeeArr.forEach(element => {
      element.casualLeavesTaken = 0;
      element.totalCasualAlloted = 12;
      element.casualLeavesRemaining = 12;

      this.dataService.getPostData(this.dataService.serviceurl + 'leave/addorupdateempleave', element).subscribe(data => {




      },
        (error => { this.handleError(error, "onResetClick()"); }));
    });
  }

  private handleError(error: any, method: any) {
    console.error('An error occurred in ViewleavesummaryComponent at method ' + method, +" " + error);
    this.blockUI.stop();
    this.notificationsService.addError('An error occurred in ViewleavesummaryComponent at method ' + method + " " + error);
  }


}
