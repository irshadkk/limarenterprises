import { Component, TemplateRef } from '@angular/core';
import { DataService } from '../../data.service';
  

@Component({
  templateUrl: 'viewattendance.component.html'
})
export class ViewAttendanceComponent {
  public employeeAttendanceArr;
  public currentItem=[];
  public statusArr=["Absent","Present","1/2Present"]
  constructor(private dataService:DataService) {
    this.loadAttendance();

  }
  
  
  loadAttendance() { 
    this.dataService.getPostData(this.dataService.serviceurl+'getAllAttandance',null).subscribe(data => { 
      this.employeeAttendanceArr=data;

     

     });
  }

  saveChanges() { 
    console.log(JSON.stringify(this.currentItem))
    this.dataService.getPostData(this.dataService.serviceurl+'updateUserAttandance',this.currentItem).subscribe(data => { 
      alert(data)

     

     });
     
  }
  onEditClick(infoModal,item) {  
     this.currentItem=item;
     console.log(JSON.stringify(this.currentItem))
     infoModal.show()
    
  }
   

}
