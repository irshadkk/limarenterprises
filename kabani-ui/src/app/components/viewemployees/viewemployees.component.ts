import { Component, TemplateRef } from '@angular/core';
import { DataService } from '../../data.service';
import { DatePipe } from '@angular/common';


@Component({
  templateUrl: 'viewemployees.component.html'
})
export class ViewEmployeesComponent {
  public employeeArr;
  public infoModal;
  public currentItem = {};
  public currentSalaryItem = [];
  public currentLeaveItem = [];
  public statusArr = ["Absent", "Present", "1/2Present"]
  constructor(private dataService: DataService, private datePipe: DatePipe) {
    this.loadEmployees();

  }


  loadEmployees() {
    this.dataService.getData(this.dataService.serviceurl + 'employee/all').subscribe(data => {
      this.employeeArr = data;



    });
  }

  saveChanges() {
    console.log(JSON.stringify(this.currentItem))
    this.dataService.getPostData(this.dataService.serviceurl + 'employee/addorupdate', this.currentItem).subscribe(data => {
      if (data === true) {
        alert("success")
        this.loadEmployees()
      } else {
        alert("failure")
        this.loadEmployees()
      }
      this.infoModal.hide()




    });

  }
  onEditClick(infoModal, item) {
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
    infoModal.show()
  }


}
