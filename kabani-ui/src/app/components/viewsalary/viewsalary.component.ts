import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { SalaryService } from './salary.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { NotificationsService } from 'angular2-notifications';

@Component({
  templateUrl: 'viewsalary.component.html',
  providers: [SalaryService]
})
export class ViewSalaryComponent implements OnInit {
  loading: boolean = false;
  public employeeSalArr;
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
  constructor(private dataService: DataService, private salaryService: SalaryService, public pushService: NotificationsService) {

  }
  ngOnInit() {
    if(this.dataService.appDefined()){
      
    }
  }
  objChanged() {
    this.employeeSalArr = [];
    this.loadSalary(this.year, this.month);
  }
  generateExcel() {
    this.salaryService.generateSalaryExcel(this.year, this.monthSelectArr.indexOf(this.month.toString()))
  }

  salaryAlreadyGenerated: boolean = false;
  loadSalary(year, month: string) {
    this.blockUI.start("Loading..");
    this.salaryAlreadyGenerated = false;
    this.loading = true;
    this.salaryService.getSalaryStatus(year, this.monthSelectArr.indexOf(month.toString()))
      .then(response => {
        if (parseInt(JSON.stringify(response)) == 0) {
          this.salaryService.generateSalary(year, month)
            .then(data => {
              this.employeeSalArr = data;
              this.loading = false;
              setTimeout(() => {
                this.blockUI.stop();
              }, 1500);
            })
            .catch(err=>this.handleError(err,"loadSalary()"));
        } else {
          this.salaryAlreadyGenerated = true;
          this.salaryService.getSalary(year, this.monthSelectArr.indexOf(month.toString()))
            .then(data => {
              this.employeeSalArr = data;
              this.loading = false;
              setTimeout(() => {
                this.blockUI.stop();
              }, 1500);
            })
            .catch(err=>this.handleError(err,"loadSalary()"));
        }
      })
      .catch(error => {
        this.handleError(error,"loadSalary()");
        this.loading = false;
        setTimeout(() => {
          this.blockUI.stop();
        }, 1500);
      })
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
  private handleError(error: any,method:any): Promise<any> {
    this.loading = false;
    console.error('An error occurred in SalaryComponent at method '+method,+" "+ error);
    this.pushService.error('Error', 'An error occurred in SalaryComponent at method '+method,+" "+ error);
    this.blockUI.stop();
    return Promise.reject(error.message || error);
  }


}
