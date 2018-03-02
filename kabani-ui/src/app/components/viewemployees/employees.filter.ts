import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'employeeFilter'
})
export class EmployeeFilter implements PipeTransform {
  transform(items: any[], searchText: string): any[] {
    if (!items) return [];
    if (!searchText) return items;
    return this.applyFilter(items, searchText.toLowerCase());
    /*searchText = searchText.toLowerCase();
    return items.filter(it => {
      return it.toLowerCase().includes(searchText);
    });*/
  }
  applyFilter(items: any[], filter: string): any[] {
    let temp: any[] = items;
    let result: any[] = [];
    let searchType: any = filter.substr(0, filter.indexOf("::"));
    let searchValue: any = filter.substr((filter.indexOf("::") + 2));
    for (let i = 0; i < temp.length; i++) {
      /*if (temp[i].userFirstName.toLowerCase().indexOf(filter.toLowerCase()) !== -1 || temp[i].userLastName.toLowerCase().indexOf(filter.toLowerCase()) !== -1 || temp[i].userEmail.toLowerCase().indexOf(filter.toLowerCase()) !== -1) {
        result.push(temp[i]);
      }*/
      if (searchType == "" || searchType == "name") {
        if (temp[i].employeeName.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
          result.push(temp[i]);
        }
      }
      else if ( searchType == "branch") {
        if (temp[i].branch.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
          result.push(temp[i]);
        }
      }
    }
    return result;
  }
}