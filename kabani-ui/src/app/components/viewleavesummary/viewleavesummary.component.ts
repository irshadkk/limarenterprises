import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { NotificationsService } from 'angular4-notify';

@Component({
  templateUrl: 'viewleavesummary.component.html',
  styleUrls: ['viewleavesummary.component.css']
})
export class ViewleavesummaryComponent implements OnInit {


  public employeeArr;
  public currentItem: any = [];
  public currentSalaryItem = [];
  public currentLeaveItem = [];
  public statusArr = ["Absent", "Present", "1/2Present"]
  @BlockUI() blockUI: NgBlockUI;
  public monthSelectArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  public yearSelectArr = ["2017", "2018", "2019", "2020", "2020", "2021",
    "2022", "2023", "2024", "2025", "2026", "2027"];
  public daysArr = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
  public days = this.daysArr[0];
  Arr = Array; //Array type captured in a variable
  public month = this.monthSelectArr[0];
  public year = this.yearSelectArr[0];
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
     this.objChanged();
    }
  }
  oddRow(rowNum: number) {
    return (rowNum % 2 == 0)?"evenRow":"";
  }
  objChanged() {
    this.employeeArr = [];
    this.days=this.daysArr[this.monthSelectArr.indexOf(this.month)];
    this.loadEmployeesAttandance(this.monthSelectArr.indexOf(this.month)+1,parseInt(this.year));
  }

  getColour(value: any) {
    if (value == "P") return "present";
    else if (value == "A") return "absent";
    else if (value == "H") return "half";
    else return "unmapped";
  }
  loadEmployeesAttandance(month: number, year: number) {
    this.blockUI.start("Loading..");
    this.dataService.getData(this.dataService.serviceurl + 'findAllAttendanceForMonth?year=' + year + "&month=" + month).subscribe(data => {
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
      if (data == true) {
        this.notificationsService.addInfo('Changes Saved');
      } else {
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
  changeinLeave() {
    this.currentItem.casualLeavesRemaining = parseInt(this.currentItem.totalCasualAlloted) - parseInt(this.currentItem.casualLeavesTaken)
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
  modifyAttandance(emp:any,day:number){
    alert('Modify Attandance of emp'+JSON.stringify(emp)+" "+day);
  }

  private handleError(error: any, method: any) {
    console.error('An error occurred in ViewleavesummaryComponent at method ' + method, +" " + error);
    this.blockUI.stop();
    this.notificationsService.addError('An error occurred in ViewleavesummaryComponent at method ' + method + " " + error);
  }


}
