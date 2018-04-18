import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { SalaryService } from '../viewsalary/salary.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
// import { NotificationsService } from 'angular4-notify';

@Component({
  selector: 'app-manage-overtime-wages',
  templateUrl: './manage-overtime-wages.component.html'
})

export class ManageOvertimeWagesComponent implements OnInit {

  loading: boolean = false;
  public wageList = [];
  public month = this.dataService.getSelectedMonth();
  public year = this.dataService.getSelectedYear();
  public monthSelectArrov = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  public yearSelectArrov= ["2017", "2018", "2019", "2020", "2020", "2021",
    "2022", "2023", "2024", "2025", "2026", "2027"];
  @BlockUI() blockUI: NgBlockUI;

  public notificationOptions = {
    position: ["bottom", "right"],
    timeOut: 5000,
    lastOnBottom: true,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
  }
  constructor(private dataService: DataService, private salaryService: SalaryService
  ) {

  }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadEmployees();
      this.objChanged();
    }
  }
  objChanged() {
    this.wageList = [];
    this.loadActiveOverTimeWages(this.year, (this.dataService.monthSelectArr.indexOf(this.month)));
  }
  loadActiveOverTimeWages(year, month: number) {
    // this.notificationsService.notifications.closed;
    this.blockUI.start("Loading..");
    this.loading = true;
    this.salaryService.loadOverTimeWages(year, month)
      .subscribe(data => {
        this.wageList = data;
        this.loading = false;
        setTimeout(() => {
          this.blockUI.stop();
        }, 1500);
      },
        error => this.handleError(error, "loadActiveOverTimeWages()")
      );
  }
  wage = { "id": "", "employeeCode": "", "employeeName": "", "wage": "", "availDate": "" }
  addNewWage(infoModal: any, isLoan: boolean) {
    this.wage = { "id": "", "employeeCode": "", "employeeName": "", "wage": "", "availDate": "" }
    infoModal.show()
  }

  saveChanges(infoModal: any) {
    this.dataService.getPostData(this.dataService.serviceurl + 'salary/addOvertimeWages', this.wage).subscribe(data => {
      this.loadActiveOverTimeWages(this.year, (this.dataService.monthSelectArr.indexOf(this.month)));
      infoModal.hide();
    });

  }
  onEditClick(infoModal, item) {
    this.wage = item;
    infoModal.show()
  }
  onDeleteClick(item) {
    this.wage = item;
    this.blockUI.start("Deleting..");
    this.dataService.getPostData(this.dataService.serviceurl + 'salary/deleteOvertimeWages', this.wage).subscribe(data => {
      if (data) {
        this.objChanged();
      }
      setTimeout(() => {
        this.blockUI.stop();
      }, 1500);
    });
  }
  employeeArr = [];
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
  onChange(event) {
    for (let emp of this.employeeArr) {
      if (emp.employeeBioDeviceCode == event.target.value) {
        this.wage.employeeName = emp.employeeName;
        this.wage.employeeCode = emp.employeeBioDeviceCode;
      }
    }
  }
  updateChanges() {
    this.dataService.getPostData(this.dataService.serviceurl + 'salary/addOvertimeWages', this.wage).subscribe(data => {
      this.loadActiveOverTimeWages(this.year, (this.dataService.monthSelectArr.indexOf(this.month)));
    });
  }


  btnDisabled() {
    if (this.wage.employeeCode == "" || this.wage.wage == "" || this.wage.availDate == "") {
      return true;
    }
    else {
      return false;
    }
  }
  private handleError(error: any, method: any) {
    // this.notificationsService.notifications.closed;
    this.loading = false;
    console.error('An error occurred in ManageLoan at method ' + method, +" " + error);
    this.blockUI.stop();
    // this.notificationsService.addError('An error occurred in ManageLoan at method ' + method + " " + error);
  }



}




