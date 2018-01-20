import { Component, TemplateRef } from '@angular/core';
import { DataService } from '../../data.service';


@Component({
  templateUrl: 'viewleavesummary.component.html'
})
export class ViewleavesummaryComponent {
  
  
  public employeeArr;
  public currentItem = [];
  public currentSalaryItem = [];
  public currentLeaveItem = [];
  public statusArr = ["Absent", "Present", "1/2Present"]
  constructor(private dataService: DataService) {
    this.loadEmployees();

  }


  loadEmployees() {
    this.dataService.getData(this.dataService.serviceurl + 'leave/allempleave').subscribe(data => {
      this.employeeArr = data;



    });
  }

  saveChanges() {
    console.log(JSON.stringify(this.currentItem))
    this.dataService.getPostData(this.dataService.serviceurl + 'employee/addorupdate', this.currentItem).subscribe(data => {
      alert(data)



    });

  }
  onEditClick(infoModal, item) {
    this.currentItem = item;
    this.dataService.getData(this.dataService.serviceurl + 'salary/allempsal/' + item.employeeCode).subscribe(data => {
      this.currentSalaryItem = data;
      console.log('currentItem==' + JSON.stringify(this.currentItem))
      console.log('currentSalaryItem==' + JSON.stringify(this.currentSalaryItem))
      this.dataService.getData(this.dataService.serviceurl + 'leave/allempleave/' + item.employeeCode).subscribe(data => {
        this.currentLeaveItem = data;
         
        console.log('currentSalaryItem==' + JSON.stringify(this.currentLeaveItem))
        infoModal.show()
      });

    });


  }
  onResetClick(){
    this.employeeArr.forEach(element => {
      element.casualLeavesTaken=0;
      element.totalCasualAlloted=12;
      element.casualLeavesRemaining=12;
      
      this.dataService.getPostData(this.dataService.serviceurl + 'leave/addorupdateempleave', element).subscribe(data => {
      



    });
    });
  }


}
