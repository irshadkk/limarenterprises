import { NgModule } from '@angular/core';
import { ChartsModule } from 'ng2-charts/ng2-charts';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { CommonModule } from '@angular/common';

import { DashboardComponent } from './dashboard.component';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DataService } from '../data.service';
import { BlockUIModule } from 'ng-block-ui';  

@NgModule({
  imports: [
    DashboardRoutingModule,
    ChartsModule,
    BsDropdownModule,
    CommonModule,BlockUIModule  
  ],
  providers:[ DataService],
  declarations: [ DashboardComponent ]
})
export class DashboardModule { }
