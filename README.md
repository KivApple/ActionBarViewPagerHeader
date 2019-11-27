# ActionBarViewPagerHeader

The custom view for ActionBar with two buttons (next page, prev page) and scrollable title and subtitle with a text from ViewPager adapter.

## Usage

Execute this command under your project root:

    git submodule add https://github.com/KivApple/ActionBarViewPagerHeader.git recyclerviewadapter

Add these lines to your Android project files:

`/settings.gradle`

    include ':app', ..., ':recyclerviewadapter'

`/build.gradle`

    buildscript {
        ext.kotlin_version = '1.3.50'
        ext.appCompatVersion = '1.0.2'
        ...
    }

`/app/build.gradle`

    dependencies {
        ...
        implementation project(':actionbarviewpagerheader')
        ...
    }
