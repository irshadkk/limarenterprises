import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { SalaryService } from '../viewsalary/salary.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { NotificationsService } from 'angular4-notify';

@Component({
  selector: 'app-manage-advance',
  templateUrl: './manage-advance.component.html',
  styleUrls: ['./manage-advance.component.scss']
})
export class ManageAdvanceComponent implements OnInit {

  loading: boolean = false;
  public advanceList = [];
  public monthSelectArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  public yearSelectArr = ["2017", "2018", "2019", "2020", "2020", "2021",
    "2022", "2023", "2024", "2025", "2026", "2027"];
  public month = this.monthSelectArr[0];
  public year = this.yearSelectArr[0];
  @BlockUI() blockUI: NgBlockUI;

  public notificationOptions = {
    position: ["bottom", "right"],
    timeOut: 5000,
    lastOnBottom: true,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
  }
  constructor(private dataService: DataService, private salaryService: SalaryService, protected notificationsService: NotificationsService) {

  }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadEmployees();
    }
  }
  objChanged() {
    this.advanceList = [];
    this.loadActiveAdvances(this.year, (this.monthSelectArr.indexOf(this.month)));
  }
  loadActiveAdvances(year, month: number) {
    this.notificationsService.notifications.closed;
    this.blockUI.start("Loading..");
    this.loading = true;
    this.salaryService.loadActiveAdvances(year, month)
      .subscribe(data => {
        this.advanceList = data;
        this.loading = false;
        setTimeout(() => {
          this.blockUI.stop();
        }, 1500);
      },
        error => this.handleError(error, "loadActiveAdvances()")
      );
  }
  loan = { "id": "", "employeeCode": "", "employeeName": "", "type": "advance", "amount": "", "availDate": "", "status": "unpaid" }
  addNewLoan(infoModal: any, isLoan: boolean) {
    if(isLoan){
      alert('This is a temp method to add loan intallment to system.')
    }
    this.loan = { "id": "", "employeeCode": "", "employeeName": "", "type": "advance", "amount": "", "availDate": "", "status": "unpaid" };
    if (isLoan) {
      this.loan.type = "loan";
    }
    infoModal.show();
  }

  saveChanges(infoModal: any) {
    this.dataService.getPostData(this.dataService.serviceurl + 'salary/addNewAdvance', this.loan).subscribe(data => {
      this.loadActiveAdvances(this.year, (this.monthSelectArr.indexOf(this.month)));
      infoModal.hide();
    });

  }
  onEditClick(infoModal, item) {
    this.loan = item;
    infoModal.show()

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
        this.loan.employeeName = emp.employeeName;
        this.loan.employeeCode = emp.employeeBioDeviceCode;
      }
    }
  }
  updateChanges() {
    if (this.loan.status == 'closed') {
      this.dataService.getPostData(this.dataService.serviceurl + 'salary/closeLoan', this.loan).subscribe(data => {
        this.loadActiveAdvances(this.year, (this.monthSelectArr.indexOf(this.month)));
      });
    }
  }
  private handleError(error: any, method: any) {
    this.notificationsService.notifications.closed;
    this.loading = false;
    console.error('An error occurred in ManageLoan at method ' + method, +" " + error);
    this.blockUI.stop();
    this.notificationsService.addError('An error occurred in ManageLoan at method ' + method + " " + error);
  }



}



