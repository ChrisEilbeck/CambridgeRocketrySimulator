## Copyright (C) 2010 S.Box
## setup_1.py

## Author: S.Box
## Created: 2011-10-27

__author__="sb4p07"
__date__ ="$21-Jan-2010 10:38:09$"

from setuptools import setup,find_packages

setup (
  name = 'RockPlot',
  version = '0.1',
  packages = find_packages(),

  # Declare your packages' dependencies here, for eg:
  install_requires=['foo>=3'],

  # Fill in these to make your Egg ready for upload to
  # PyPI
  author = 'sb4p07',
  author_email = '',

  summary = 'Just another Python package for the cheese shop',
  url = '',
  license = '',
  long_description= 'Long description of the package',

  # could also include long_description, download_url, classifiers, etc.


)
