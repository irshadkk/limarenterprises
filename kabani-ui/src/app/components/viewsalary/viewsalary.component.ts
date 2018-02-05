import { Component, TemplateRef } from '@angular/core';
import { DataService } from '../../data.service';
import { SalaryService } from './salary.service';


@Component({
  templateUrl: 'viewsalary.component.html',
  providers: [SalaryService]
})
export class ViewSalaryComponent {
  loading: boolean = false;
  public employeeSalArr;
  public currentItem = [];
  public monthSelectArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  public yearSelectArr = ["2017", "2018", "2019", "2020", "2020", "2021",
    "2022", "2023", "2024", "2025", "2026", "2027"];
  public month = this.monthSelectArr[0];
  public year = this.yearSelectArr[0];
  constructor(private dataService: DataService, private salaryService: SalaryService) {

  }
  objChanged() {
    this.employeeSalArr=[];
    this.loadSalary(this.year, this.month);
  }


  loadSalary(year, month: string) {
    this.loading = true;
    this.salaryService.getSalaryStatus(year, this.monthSelectArr.indexOf(month.toString()))
      .then(response => {
        if (parseInt(JSON.stringify(response)) == 0) {
          this.salaryService.generateSalary(year, month)
            .then(data => {
              this.employeeSalArr = data;
              this.loading = false;
            })
        } else {
          alert('Salary Generated for selected month')
          this.loading = false;
        }
      })
      .catch(error => {
        alert('Error Occured during salary generation')
        this.loading = false;
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


}
