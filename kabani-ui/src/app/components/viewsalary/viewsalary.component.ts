import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { SalaryService } from './salary.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { NotificationsService } from 'angular4-notify';

@Component({
  templateUrl: 'viewsalary.component.html',
  providers: [SalaryService]
})
export class ViewSalaryComponent implements OnInit {
  loading: boolean = false;
  public employeeSalArr;
  public employeeSalStatusArr;
  public currentItem = [];
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
      this.loadAllStatus();

    }
  }
  objChanged() {
    this.employeeSalArr = [];
    this.loadSalary(this.year, this.month);
  }
  generateExcel(year,month) {
    this.salaryService.generateSalaryExcel(year, month)
  }
  loadAllStatus(){
    
    this.blockUI.start("loading.."); 
    this.dataService.getData(this.dataService.serviceurl + 'salary/salaryStatusAll').subscribe(data => {
     this.employeeSalStatusArr=data; 
     this.blockUI.stop()
    },
    (error => { this.handleError(error,"saveChanges()");}));
  
  } 
  resetAll(){
    
    this.blockUI.start("loading.."); 
    this.dataService.getData(this.dataService.serviceurl + 'salary/salaryStatusResetAll').subscribe(data => {
      this.loadAllStatus()
     this.blockUI.stop()
    },
    (error => { this.handleError(error,"saveChanges()");}));
  
  }
  deletItem(item) {
    this.blockUI.start("Saving.."); 
    this.dataService.getPostData(this.dataService.serviceurl + 'salary/salaryStatusRemove', item).subscribe(data => {
     
    },
    (error => { this.handleError(error,"saveChanges()");}));
  }
  

  salaryAlreadyGenerated: boolean = false;
  loadSalary(year, month: string) {
    this.notificationsService.notifications.closed;
    this.blockUI.start("Loading..");
    this.salaryAlreadyGenerated = false;
    this.loading = true;
    this.salaryService.getSalaryStatus(year, this.monthSelectArr.indexOf(month.toString()))
      .subscribe(response => {
        if (parseInt(JSON.stringify(response)) == 0) {
          this.salaryService.generateSalary(year, month)
            .subscribe(data => {
              this.employeeSalArr = data;
              this.loading = false;
              this.notificationsService.addInfo('Salary Generated for ' + year + " month " + month)
              setTimeout(() => {
                this.blockUI.stop();
              }, 1500);
              this.loadAllStatus();
            },
            error => this.handleError(error, "loadSalary()")
            );
          
        } else {
          this.salaryAlreadyGenerated = true;
          this.salaryService.getSalary(year, this.monthSelectArr.indexOf(month.toString()))
            .subscribe(data => {
              this.employeeSalArr = data;
              this.loading = false;
              this.notificationsService.addInfo('Retrieved Salary Generated for ' + year + " month " + month)
              setTimeout(() => {
                this.blockUI.stop();
              }, 1500);
              this.loadAllStatus();
            }
            , err => this.handleError(err, "loadSalary()")
            );
        }
          
      },
      error => {
        this.handleError(error, "loadSalary()");
        this.loading = false;
        setTimeout(() => {
          this.blockUI.stop();
        }, 1500);
      })
  }
  /*
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
  
    }*/
  private handleError(error: any, method: any) {
    this.loadAllStatus();
    this.notificationsService.notifications.closed;
    this.loading = false;
    console.error('An error occurred in SalaryComponent at method ' + method, +" " + error);
    this.blockUI.stop();
    this.notificationsService.addError('An error occurred in SalaryComponent at method ' + method + " " + error);
  }


}
