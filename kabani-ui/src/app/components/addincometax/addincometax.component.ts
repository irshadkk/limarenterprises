import { Component, TemplateRef, OnInit, ViewContainerRef, ViewChild } from '@angular/core';
import { DataService } from '../../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
// import { NotificationsService } from 'angular4-notify';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';
import { ToastOptions } from 'ng2-toastr';
import { DatePipe } from '@angular/common';

@Component({
  templateUrl: 'addincometax.component.html'
})
export class AddIncomeTaxComponent implements OnInit {

  public monthSelectArrIT = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  public yearSelectArrIT = ["2017", "2018", "2019", "2020", "2020", "2021",
    "2022", "2023", "2024", "2025", "2026", "2027"];
     public month = this.dataService.getSelectedMonth();
  public year = this.dataService.getSelectedYear();
  objChanged() {
    this.employeeTaxArr = [];
    this.loadIncomeTax(this.year, this.month);
  }
  public employeeArr;
  public employeeTaxArr;
  public currentItem ={
    employeeCode:'',
    employeeName:'',
    monthlySalry:0,
    taxAmount:0,
    taxForTheYear:'',
    taxForTheMonth:'',
    status:''
   } ;
  public infoModal;
  public searchText;
  public currentItem_employeeCode;
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
  // constructor(private dataService: DataService, private datePipe: DatePipe, protected notificationsService: NotificationsService, public toastr: ToastsManager, vcr: ViewContainerRef) {
  constructor(private dataService: DataService, private datePipe: DatePipe, public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadEmployees();
      this.loadIncomeTax(this.year, this.month);
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
  loadIncomeTax(year, month: string) {
    this.blockUI.start("Loading..");
    this.dataService.getData(this.dataService.serviceurl + 'tax/income/all/'+(this.monthSelectArrIT.indexOf(this.month)+1)+'/'+this.year).subscribe(data => {
      this.employeeTaxArr = data;
      setTimeout(() => {
        this.blockUI.stop();
      }, 1500);


    },
      (error => { this.handleError(error, "loadEmployees()"); }));
  }

  saveChanges() {
    this.blockUI.start("Saving..");
    this.dataService.getPostData(this.dataService.serviceurl + 'tax/income/addorupdate', this.currentItem).subscribe(data => {
      if (data == true) {
        // this.notificationsService.addInfo('Changes Saved');
         this.loadIncomeTax(this.year, this.month);
        this.toastr.success(' Changes Saved', 'Success!', { duration: 1500, dismiss: 'auto' });
      } else {
        // this.notificationsService.addWarning('Changes couldnt be saved');
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
    this.infoModal = infoModal;
    this.currentItem = item;
    item.dayOfextraDay = this.datePipe.transform(item.dayOfextraDay, 'yyyy-MM-dd');
    item.dayOfOtherExtraDay = this.datePipe.transform(item.dayOfOtherExtraDay, 'yyyy-MM-dd');

    infoModal.show();
    setTimeout(() => {
      this.blockUI.stop();
    }, 100);
  } 

  onAddClick(infoModal) {
    this.currentItem ={
    employeeCode:'',
    employeeName:'',
    monthlySalry:0,
    taxAmount:0,
    taxForTheYear:'',
    taxForTheMonth:'',
    status:''
   };
    this.infoModal = infoModal;

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
    // this.notificationsService.addError('An error occurred in ViewleavesummaryComponent at method ' + method + " " + error);
  }


}
