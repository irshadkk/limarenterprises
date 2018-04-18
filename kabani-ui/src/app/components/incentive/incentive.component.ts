import { Component, TemplateRef, OnInit } from '@angular/core';
import { HttpClient, HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadFileService } from '../upload/upload-file.service';
import { DataService } from '../../data.service';
import { SalaryService } from '../viewsalary/salary.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
// import { NotificationsService } from 'angular4-notify';

@Component({
  selector: 'app-incentive',
  templateUrl: './incentive.component.html',
  styleUrls: ['./incentive.component.scss']
})
export class IncentiveComponent implements OnInit {

  loading: boolean = false;
  public incentiveList = [];
  public monthSelectArrmi = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  public yearSelectArrmi= ["2017", "2018", "2019", "2020", "2020", "2021",
    "2022", "2023", "2024", "2025", "2026", "2027"];
  public month =this.dataService.getSelectedMonth();
  public year = this.dataService.getSelectedYear();
  @BlockUI() blockUI: NgBlockUI;

  public notificationOptions = {
    position: ["bottom", "right"],
    timeOut: 5000,
    lastOnBottom: true,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
  }
  constructor(private uploadService: UploadFileService,private dataService: DataService, private salaryService: SalaryService ) {

  }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadEmployees();
      this.objChanged();
    }
  }
  objChanged() {
    this.incentiveList = [];
    this.loadIncentives(this.year, (this.dataService.monthSelectArr.indexOf(this.month) ));
  }
  loadIncentives(year, month: number) {
    // this.notificationsService.notifications.closed;
    this.blockUI.start("Loading..");
    this.loading = true;
    this.salaryService.loadIncentivesForMonth(year, month)
      .subscribe(data => {
        this.incentiveList = data;
        this.loading = false;
        setTimeout(() => {
          this.blockUI.stop();
        }, 1500);
      },
        error => this.handleError(error, "loadIncentives()")
      );
  }
  incentive = { "id": "", "employeeCode": "", "employeeName": "", "amount": "", "date": "" }
  addNewIncentive(infoModal: any) {
    this.incentive = { "id": "", "employeeCode": "", "employeeName": "", "amount": "", "date": "" };
    infoModal.show();
  }

  saveChanges() {
     this.dataService.getPostData(this.dataService.serviceurl + 'salary/addNewIncentive', this.incentive).subscribe(data => {
      this.loadIncentives(this.year, (this.dataService.monthSelectArr.indexOf(this.month)));
    });

  }
  onEditClick(infoModal, item) {
    this.incentive = item;
    infoModal.show(); 
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
      if (emp.employeeCode == event.target.value) {
        this.incentive.employeeName = emp.employeeName;
        this.incentive.employeeCode = emp.employeeCode;      }
    }
  }
  selectedFiles: FileList
  currentFileUpload: File
  progress: { percentage: number } = { percentage: 0 }
  selectFile(event) {
    this.selectedFiles = event.target.files;
  } 
xxx:any;
  upload() {
    this.progress.percentage = 0;
    let url = this.dataService.serviceurl + '/salary/addNewIncentiveBulk';
    this.currentFileUpload = this.selectedFiles.item(0)
    this.uploadService.pushFileToStorage(this.currentFileUpload, url).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        this.xxx="";
        this.selectedFiles = null;
        this.currentFileUpload=null;
       this.loadIncentives(new Date().getFullYear(),new Date().getMonth())
      }
    })

   
  }   btnDisabled(){
    if(this.incentive.employeeCode=="" || this.incentive.amount==""  || this.incentive.date== ""){
      return true;
    }
    else{
      return false;
    }
  }
  private handleError(error: any, method: any) {
    // this.notificationsService.notifications.closed;
    this.loading = false;
    console.error('An error occurred in IncentiveComponent at method ' + method, +" " + error);
    this.blockUI.stop();
    // this.notificationsService.addError('An error occurred in IncentiveComponent at method ' + method + " " + error);
  }

}




