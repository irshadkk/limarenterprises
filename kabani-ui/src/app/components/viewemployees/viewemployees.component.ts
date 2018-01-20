import { Component, TemplateRef } from '@angular/core';
import { DataService } from '../../data.service';


@Component({
  templateUrl: 'viewemployees.component.html'
})
export class ViewEmployeesComponent {
  public employeeArr;
  public currentItem = [];
  public currentSalaryItem = [];
  public currentLeaveItem = [];
  public statusArr = ["Absent", "Present", "1/2Present"]
  constructor(private dataService: DataService) {
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


}
