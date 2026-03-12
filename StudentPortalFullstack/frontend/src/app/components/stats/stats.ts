import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { StatsService } from '../../services/stats';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.html',
  styleUrls: ['./stats.css'],
  standalone: false
})
export class Stats {
  @Input() type: 'strength' | 'standardCount' | 'result' = 'strength';

  // Dynamic inputs bound from the template
  gender: string = '';
  standardOrClass?: number;
  schoolName?: number;
  pass?: boolean;

  title: string = '';
  value: any = null;
  isArray: boolean = false;
  loading: boolean = false;

  constructor(private statsService: StatsService, private cd: ChangeDetectorRef) {}

  loadStats() {
    this.value = null;
    this.loading = true;

    switch (this.type) {
      case 'strength':
        if (this.gender && this.standardOrClass !== undefined) {
          this.title = `Strength (${this.gender}, Class ${this.standardOrClass})`;
          this.statsService.getStrength(this.gender, this.standardOrClass).subscribe({
            next: (data: any) => this.setValue(data),
            error: () => this.loading = false
          });
        } else {
          this.loading = false;
        }
        break;

      case 'standardCount':
        if (this.standardOrClass !== undefined) {
          this.title = `Standard Count (Class ${this.standardOrClass})`;
          this.statsService.getStandardCount(this.standardOrClass).subscribe({
            next: data => this.setValue(data),
            error: () => this.loading = false
          });
        } else {
          this.loading = false;
        }
        break;

      case 'result':
        if (this.pass !== undefined) {
          this.title = `Results (pass/fail ${this.pass})`;
          this.statsService.getResult(this.pass).subscribe({
            next: (data: any) => this.setValue(data),
            error: () => this.loading = false
          });
        } else {
          this.loading = false;
        }
        break;
    }
  }

  private setValue(data: any) {
    this.value = data;
    this.isArray = Array.isArray(data);
    this.loading = false;
    this.cd.detectChanges();
  }
}