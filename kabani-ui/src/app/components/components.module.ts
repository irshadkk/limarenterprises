import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../filters/filter.pipe';
import { EmployeeFilter } from './viewemployees/employees.filter';
import { DatePipe } from '@angular/common';






// Forms Component

import { UploadFileService } from './upload/upload-file.service';
import { UploadCsvComponent } from './uploadcsv/uploadcsv.component';

import { ListUploadComponent } from './upload/list-upload/list-upload.component';
import { EmployeeUploadComponent } from './upload/employee-upload/employee-upload.component';
import { IncomeTaxUploadComponent } from './upload/incometax-upload/incometax-upload.component';


import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { SwitchesComponent } from './switches.component';
import { ViewAttendanceComponent } from './viewattendance/viewattendance.component';
import { ViewSalaryComponent } from './viewsalary/viewsalary.component';
import { ViewEmployeesComponent } from './viewemployees/viewemployees.component';
import { ViewleavesummaryComponent } from './viewleavesummary/viewleavesummary.component';
import { AddholidaysComponent } from './addholidays/addholidays.component';
import { AddPresentComponent } from './addpresent/addpresent.component';

import { AddIncomeTaxComponent } from './addincometax/addincometax.component'; 



// Modal Component
import { ModalModule } from 'ngx-bootstrap/modal';

// Tabs Component
import { TabsModule } from 'ngx-bootstrap/tabs';

// Components Routing
import { ComponentsRoutingModule } from './components-routing.module';
import { BlockUIModule } from 'ng-block-ui';
import { ManageLoanComponent } from './manage-loan/manage-loan.component';
import { ManageAdvanceComponent } from './manage-advance/manage-advance.component';
import { SalaryService } from './viewsalary/salary.service';
import { HalfMonthComponent } from './viewsalary/half-month/half-month.component';

@NgModule({
  imports: [
    ComponentsRoutingModule,
    BsDropdownModule.forRoot(),
    ModalModule.forRoot(),
    TabsModule, HttpModule, CommonModule, FormsModule, ReactiveFormsModule, HttpClientModule, BlockUIModule
  ],
  declarations: [
    UploadCsvComponent, ListUploadComponent, EmployeeUploadComponent,
    SwitchesComponent,
    ViewAttendanceComponent, ViewEmployeesComponent, ViewleavesummaryComponent,
    AddholidaysComponent, AddPresentComponent,AddIncomeTaxComponent,IncomeTaxUploadComponent,
    FilterPipe,EmployeeFilter,
    ViewSalaryComponent,
    ManageLoanComponent,
    ManageAdvanceComponent,
    HalfMonthComponent
  ],
  providers: [UploadFileService, DatePipe, SalaryService]
})
export class ComponentsModule { }
