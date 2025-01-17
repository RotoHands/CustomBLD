
import glob

def merge_files(file_list, output_file):
    with open(output_file, 'wb') as outfile:
        for file_name in file_list:
            try:
                with open(file_name, 'rb') as infile:
                    outfile.write(infile.read())
                print(f"Successfully merged: {file_name}")
            except Exception as e:
                print(f"Error processing {file_name}: {e}")


def main():
    # List of input files
    input_files = glob.glob('txt_files\\file*.txt')
    # Output file
    output_file = 'txt_files\\all_333_scambles.txt'
    print(input_files)
    merge_files(input_files, output_file)
    print(f"All files have been combined into {output_file}")

if __name__ == '__main__':
    main()