'use strict';

var gulp = require('gulp');

gulp.task('default', function () {
  gulp.src('*.js')
    .pipe(gulp.dest('*.js'));
});

gulp.task('hello', function(){
  console.log('Hello World!!!');
});

gulp.task('try', ['hello']);
