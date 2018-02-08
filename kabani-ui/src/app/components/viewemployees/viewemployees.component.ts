import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { DatePipe } from '@angular/common';
import { BlockUI, NgBlockUI } from 'ng-block-ui'; 
import { NotificationsService } from 'angular4-notify';


@Component({
  templateUrl: 'viewemployees.component.html'
})
export class ViewEmployeesComponent implements OnInit {
  public employeeArr;
  public infoModal;
  public currentItem = {};
  public currentSalaryItem = [];
  public currentLeaveItem = [];
  public statusArr = ["Absent", "Present", "1/2Present"];
  @BlockUI() blockUI: NgBlockUI;
 
  constructor(private dataService: DataService, private datePipe: DatePipe,protected notificationsService: NotificationsService) {


  }
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
    (error => { this.handleError(error,"loadEmployees()");}));
  }

  saveChanges() {
    this.blockUI.start("Saving..");
    console.log(JSON.stringify(this.currentItem))
    this.dataService.getPostData(this.dataService.serviceurl + 'employee/addorupdate', this.currentItem).subscribe(data => {
      if (data === true) {
        this.loadEmployees()
      } else {
        this.loadEmployees()
      }
      this.infoModal.hide()
    },
    (error => { this.handleError(error,"saveChanges()");}));

  }
  onEditClick(infoModal, item) {
    this.blockUI.start("Loading..");
    this.infoModal = infoModal;
    if (item) {
      console.log(JSON.stringify(item))

      item.dateOfBirth = this.datePipe.transform(item.dateOfBirth, 'yyyy-MM-dd');
      item.dateOfJoining = this.datePipe.transform(item.dateOfJoining, 'yyyy-MM-dd');
      console.log(JSON.stringify(item.dateOfBirth))
      this.currentItem = item;
    }
    else {
      this.currentItem = {
        employeeCode: '',
        employeeBioDeviceCode: '',
        employeeName: '',
        branch: '',
        designation: '',
        department: '',
        employeeAge: '',
        employeeSex: '',
        dateOfBirth: '',
        nameOfGuardian: '',
        designationCode: '',
        dateOfJoining: '',
        mobileNumber: '',
        emailId: '',
        bankName: '',
        ifscCode: '',
        bankAccountNumber: '',
        hra: '',
        da: '',
        basic: '',
        salary: '',
        totalCasualAlloted: '',
        casualLeavesTaken: '',
        casualLeavesRemaining: '',
        cityCompensationAllowence: '',
        numberOfWeeklyOffGranted: '',
        numberOfLeaveGranted: '',
        overTimeWages: '',
        leaveWages: '',
        nationalAndFestivalHolidayWages: '',
        arrearPaid: '',
        bonus: '',
        maternityBenefit: '',
        otherAllowances: '',
        totalStaffAdvance: '',
        totalSalaryAdvance: '',
        advanceTotalAmount: '',
        deductionOfFine: '',
        deductionForLossAndDamages: '',
        totalLineShort: '',
        otherDeduction: '',
        totalDeduction: ''

      };
    }
    infoModal.show(); setTimeout(() => {
      this.blockUI.stop();
    }, 1500);
  }

  private handleError(error: any, method: any) {
    console.error('An error occurred in ViewEmployeesComponent at method ' + method, +" " + error);
    this.blockUI.stop();
    this.notificationsService.addError('An error occurred in ViewEmployeesComponent at method ' + method + " " + error);
   }


}
