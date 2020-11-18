var localParams = require('./localparams.json');
var proxyName = localParams.proxyName;
var cssfileDestPath = '../../src/main/resources/static/css/';
var cssfileDestName = 'style.css';
var sassFileSourcePath = '../scss/';
var sassFileSourceName = 'style.scss';

var jsSourcesFile = './js_files.json';
var jsSources = require(jsSourcesFile);
var jsfileDestPath = '../../src/main/resources/static/js/';
var jsfileDestFileName = 'cms.js';

var cssMinfileDestName = 'style.min.css';
var jsMinfileName = 'site.min.js';
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

var gulp  = require('gulp'),
    rename = require('gulp-rename'),
    plumber = require('gulp-plumber'),
    notify = require('gulp-notify'),
    beep = require('beepbeep'),
    concat = require('gulp-concat'),
    clear = require('clear'),
    terser = require('gulp-terser'),
    decomment = require('gulp-decomment'),
	minify = require('gulp-minify');
    sass   = require('gulp-sass'),
    postcss = require('gulp-postcss'),
    autoprefixer = require('autoprefixer'),
    pixrem  = require('pixrem'),
    quantityQueries = require('postcss-quantity-queries'),
    flexbugsFixes = require('postcss-flexbugs-fixes'),
    cssnano = require("cssnano"),
    cleanCSS = require('gulp-clean-css'),

    browserSync = require('browser-sync').create();

var onError = function(err) {
    notify.onError({
        title:    "Gulp error in " + err.plugin,
        message:  err.toString()
    })(err);
    beep();
    //console.log('\u0007');
    this.emit('end');
};


//////////////////////////////////////////////////
// SASS task >>
//////////////////////////////////////////////////
function _sass() {
    var postcssPlugins = [
        autoprefixer(),
        pixrem(),
        quantityQueries(),
        flexbugsFixes()
    ];

    return (
        gulp
            .src(sassFileSourcePath + sassFileSourceName)
            .pipe(plumber({ errorHandler: onError }))
            // Use sass with the files found, and log any errors
            .pipe(sass())
            .on("error", sass.logError)
            .pipe(postcss(postcssPlugins))
            .pipe(rename(cssfileDestName))

            // What is the destination for the compiled file?
            .pipe(gulp.dest(cssfileDestPath))
            .pipe(browserSync.stream())
    );
}
exports._sass = _sass;
//////////////////////////////////////////////////
// SASS task <<
//////////////////////////////////////////////////

//////////////////////////////////////////////////
// JS task >>
//////////////////////////////////////////////////
function js()  {

    return (
        gulp
        .src(jsSources)
        .pipe(plumber({ errorHandler: onError }))
        .pipe(concat(jsfileDestFileName, {newLine: ';'}))
         .pipe(minify())
		.pipe(gulp.dest(jsfileDestPath))
        .pipe(browserSync.stream())
    );
}
exports.js = js;
//////////////////////////////////////////////////
// JS task <<
//////////////////////////////////////////////////

//////////////////////////////////////////////////
// build >>
//////////////////////////////////////////////////
function build()  {
    _sass();
    js();

    process.stdout.write('Build start.' + '\n');

    gulp.
    src(jsfileDestPath + jsfileDestFileName)
        .pipe(decomment({trim: true}))
        .pipe(terser({
            warnings : 'verbose'
        }))
        .pipe(rename(jsMinfileName))
        .pipe(gulp.dest(jsfileDestPath));

    gulp.
    src(cssfileDestPath + cssfileDestName)
        .pipe(cleanCSS())
        .pipe(rename(cssMinfileDestName))
        .pipe(gulp.dest(cssfileDestPath));


    process.stdout.write('Build done.' + '\n');
}

exports.build = build;
//////////////////////////////////////////////////
// build <<
//////////////////////////////////////////////////

// watch task >>
function watchTask(){
    // gulp.watch takes in the location of the files to watch for changes
    // and the name of the function we want to run on change
    var __tmp = [].concat(jsSourcesFile, jsSources);

    browserSync.init({
        ghostMode: {
            clicks: false,
            forms: false,
            location : false,
            scroll: false
        },
        ui: {
            port: 9090,
            weinre: {
                port: 9191
            }
        },
        scrollProportionally: false,
        proxy: proxyName
    });

    _sass();
    js();

    gulp.watch([(sassFileSourcePath + '**/*.scss')], gulp.series(_sass)).on('change',browserSync.reload);
    gulp.watch(__tmp, gulp.series(js)).on('change',browserSync.reload);
}
exports.watchTask = watchTask;
