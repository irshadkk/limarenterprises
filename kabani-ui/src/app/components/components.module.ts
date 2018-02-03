import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HttpModule } from '@angular/http';
import {HttpClientModule} from '@angular/common/http'; 
import { CommonModule } from '@angular/common';
import { FilterPipe} from '../filters/filter.pipe';
import { DatePipe } from '@angular/common';




 

// Forms Component

import {UploadFileService} from './upload/upload-file.service';
import { UploadCsvComponent } from './uploadcsv/uploadcsv.component';

import {DetailsUploadComponent} from './upload/details-upload/details-upload.component';
import {FormUploadComponent} from './upload/form-upload/form-upload.component';
import {ListUploadComponent} from './upload/list-upload/list-upload.component';
import { EmployeeUploadComponent } from './upload/employee-upload/employee-upload.component';



import { BsDropdownModule } from 'ngx-bootstrap/dropdown'; 
import { SwitchesComponent } from './switches.component';
import { ViewAttendanceComponent } from './viewattendance/viewattendance.component';
import { ViewSalaryComponent } from './viewsalary/viewsalary.component';
import { ViewEmployeesComponent } from './viewemployees/viewemployees.component';
import { ViewleavesummaryComponent } from './viewleavesummary/viewleavesummary.component'; 
import { AddholidaysComponent } from './addholidays/addholidays.component'; 


// Modal Component
import { ModalModule } from 'ngx-bootstrap/modal'; 

// Tabs Component
import { TabsModule } from 'ngx-bootstrap/tabs'; 

// Components Routing
import { ComponentsRoutingModule } from './components-routing.module';

@NgModule({
  imports: [
    ComponentsRoutingModule,
    BsDropdownModule.forRoot(),
    ModalModule.forRoot(),
    TabsModule,HttpModule,CommonModule,FormsModule, ReactiveFormsModule,HttpClientModule 
  ],
  declarations: [ 
    UploadCsvComponent,DetailsUploadComponent,FormUploadComponent,ListUploadComponent,EmployeeUploadComponent,
    SwitchesComponent,
    ViewAttendanceComponent,ViewEmployeesComponent,ViewleavesummaryComponent,AddholidaysComponent,
    FilterPipe,
    ViewSalaryComponent
  ],
   providers: [UploadFileService,DatePipe]
})
export class ComponentsModule { }
