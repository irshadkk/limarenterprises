import { Component, OnInit, Input } from '@angular/core';
import { DataService } from '../../../data.service';

@Component({
  selector: 'details-upload',
  templateUrl: './details-upload.component.html',
  styleUrls: ['./details-upload.component.css']
})
export class DetailsUploadComponent implements OnInit {

  @Input() fileUpload: string;

  constructor(private dataService: DataService) { }

  ngOnInit() {
    if (this.dataService.appDefined()) {

    }
  }

}
