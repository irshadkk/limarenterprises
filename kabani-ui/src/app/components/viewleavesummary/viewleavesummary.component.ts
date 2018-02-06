import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';


@Component({
  templateUrl: 'viewleavesummary.component.html'
})
export class ViewleavesummaryComponent implements OnInit {


  public employeeArr;
  public currentItem = [];
  public currentSalaryItem = [];
  public currentLeaveItem = [];
  public statusArr = ["Absent", "Present", "1/2Present"]
  @BlockUI() blockUI: NgBlockUI;

  constructor(private dataService: DataService) { }
  ngOnInit() {
    if (this.dataService.appDefined()) {
      this.loadEmployees();
    }
  }


  loadEmployees() {
    this.blockUI.start("Loading..");
    this.dataService.getData(this.dataService.serviceurl + 'leave/allempleave').subscribe(data => {
      this.employeeArr = data;
      setTimeout(() => {
        this.blockUI.stop();
      }, 1500);


    });
  }

  saveChanges() {
    this.blockUI.start("Saving..");
    this.dataService.getPostData(this.dataService.serviceurl + 'employee/addorupdate', this.currentItem).subscribe(data => {
      setTimeout(() => {
        this.blockUI.stop();
      }, 1500);
    });

  }
  onEditClick(infoModal, item) {
    this.blockUI.start("Loading..");
    this.currentItem = item;
    this.dataService.getData(this.dataService.serviceurl + 'salary/allempsal/' + item.employeeCode).subscribe(data => {
      this.currentSalaryItem = data;
      console.log('currentItem==' + JSON.stringify(this.currentItem))
      console.log('currentSalaryItem==' + JSON.stringify(this.currentSalaryItem))
      this.dataService.getData(this.dataService.serviceurl + 'leave/allempleave/' + item.employeeCode).subscribe(data => {
        this.currentLeaveItem = data;

        console.log('currentSalaryItem==' + JSON.stringify(this.currentLeaveItem))
        infoModal.show()
        setTimeout(() => {
          this.blockUI.stop();
        }, 1500);
      });

    });


  }
  onResetClick() {
    this.employeeArr.forEach(element => {
      element.casualLeavesTaken = 0;
      element.totalCasualAlloted = 12;
      element.casualLeavesRemaining = 12;

      this.dataService.getPostData(this.dataService.serviceurl + 'leave/addorupdateempleave', element).subscribe(data => {




      });
    });
  }


}
