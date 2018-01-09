import { Component, TemplateRef } from '@angular/core';
import { DataService } from '../../data.service';
  

@Component({
  templateUrl: 'viewsalary.component.html'
})
export class ViewSalaryComponent {
  public employeeSalArr;
  public currentItem=[];
  public monthSelectArr=["Jan","Feb","Mar","Apr","May","Jun",
  "Jul","Aug","Sep","Oct","Nov","Dec"];
  public yearSelectArr=["2017","2018","2019","2020","2020","2021",
  "2022","2023","2024","2025","2026","2027"];
  public month=this.monthSelectArr[0];
  public year=this.yearSelectArr[0];
  constructor(private dataService:DataService) { 

  }
  objChanged(){
    this.loadAttendance(this.year,this.month);
  }
  
  
  loadAttendance(year,month) { 
  this.employeeSalArr=[]; 
    this.dataService.getPostData('http://kabanip-dev.us-east-1.elasticbeanstalk.com/getSalary/'+year+'/'+month,null).subscribe(data => { 
       this.employeeSalArr=data;
      console.log("---------------")
      console.log(data)
     console.log("---------------") 

     

     });
   }  

  saveChanges() { 
    console.log(JSON.stringify(this.currentItem))
    this.dataService.getPostData('http://kabanip-dev.us-east-1.elasticbeanstalk.com/updateUserAttandance',this.currentItem).subscribe(data => { 
      alert(data)

     

     });
     
  }
  onEditClick(infoModal,item) {  
     this.currentItem=item;
     console.log(JSON.stringify(this.currentItem))
     infoModal.show()
    
  }
   

}
