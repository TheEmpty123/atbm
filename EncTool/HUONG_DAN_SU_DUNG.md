# HƯỚNG DẪN SỬ DỤNG CÔNG CỤ MÃ HÓA (ENCRYPTION TOOL)

## Giới thiệu

Công cụ Mã hóa (Encryption Tool) là một ứng dụng Java đơn giản cung cấp nhiều khả năng mã hóa và giải mã khác nhau. Ứng dụng này được thiết kế với giao diện đồ họa thân thiện, giúp người dùng dễ dàng thực hiện các tác vụ mã hóa mà không cần kiến thức chuyên sâu về mật mã học.

## Cài đặt

### Yêu cầu hệ thống
- Hệ điều hành Windows 64-bit
- Không cần cài đặt Java riêng (JRE đã được tích hợp trong tệp thực thi)

### Cách cài đặt
1. Tải tệp thực thi `EncryptionTool.exe` từ thư mục `target`
2. Đảm bảo tệp `java.policy` nằm trong cùng thư mục với tệp thực thi
3. Chạy ứng dụng bằng cách nhấp đúp vào tệp `EncryptionTool.exe`

Bạn cũng có thể tạo shortcut cho tệp thực thi:
1. Nhấp chuột phải vào `EncryptionTool.exe` trong thư mục `target`
2. Chọn "Tạo shortcut"
3. Di chuyển shortcut đến desktop hoặc vị trí thuận tiện khác

## Giao diện chính

Khi khởi động, ứng dụng hiển thị cửa sổ chính với ba tab:
1. **Traditional** (Mã hóa truyền thống)
2. **Symmetric** (Mã hóa đối xứng)
3. **Asymmetric** (Mã hóa bất đối xứng)

Mỗi tab cung cấp các phương pháp mã hóa khác nhau với các tùy chọn riêng.

## 1. Tab Mã hóa truyền thống (Traditional)

Tab này cung cấp các phương pháp mã hóa cổ điển:

### Các thuật toán có sẵn:
- **Caesar Cipher**: Mã hóa bằng cách dịch chuyển các ký tự theo một giá trị cố định
- **Affine Cipher**: Mã hóa sử dụng hàm toán học affine
- **Vigenère Cipher**: Mã hóa sử dụng nhiều bảng chữ cái Caesar
- **Substitution Cipher**: Mã hóa bằng cách thay thế mỗi ký tự bằng một ký tự khác
- **Hill Cipher**: Mã hóa sử dụng đại số tuyến tính
- **Transposition Cipher**: Mã hóa bằng cách sắp xếp lại vị trí các ký tự

### Cách sử dụng:
1. Chọn thuật toán từ danh sách thả xuống
2. Nhập khóa theo định dạng phù hợp với thuật toán đã chọn:
   - **Caesar Cipher**: Nhập giá trị số làm khóa (ví dụ: "3")
   - **Affine Cipher**: Nhập khóa theo định dạng "a,b" (ví dụ: "5,8") trong đó cả hai giá trị đều là số nguyên và 'a' là số nguyên tố cùng nhau với 26
   - **Vigenère Cipher**: Nhập bất kỳ khóa văn bản nào bao gồm các chữ cái
   - **Substitution Cipher**: Nhập khóa gồm 26 chữ cái đại diện cho toàn bộ bảng chữ cái thay thế
   - **Hill Cipher**: Nhập ma trận số nguyên với kích thước nxn, được định dạng dưới dạng các giá trị hàng được phân tách bằng dấu phẩy, với các hàng được phân tách bằng dấu chấm phẩy
   - **Transposition Cipher**: Nhập một số đại diện cho số cột
3. Nhập văn bản cần mã hóa vào ô "Input Text"
4. Nhấn nút "Encrypt" để mã hóa hoặc "Decrypt" để giải mã
5. Kết quả sẽ hiển thị trong ô "Output Text"

## 2. Tab Mã hóa đối xứng (Symmetric)

Tab này cung cấp các thuật toán mã hóa đối xứng hiện đại:

### Các thuật toán có sẵn:
- **AES**: Advanced Encryption Standard, tiêu chuẩn mã hóa phổ biến nhất hiện nay
- **DES**: Data Encryption Standard, thuật toán mã hóa cũ hơn
- **3DES**: Triple DES, phiên bản cải tiến của DES
- **Blowfish**: Thuật toán mã hóa khối nhanh
- **RC4**: Thuật toán mã hóa dòng đơn giản
- **Twofish**: Thuật toán mã hóa khối an toàn (yêu cầu Bouncy Castle provider)

### Chế độ hoạt động:
Tùy thuộc vào thuật toán, có thể có các chế độ khác nhau:
- **ECB** (Electronic Codebook): Chế độ đơn giản nhất, mỗi khối được mã hóa độc lập
- **CBC** (Cipher Block Chaining): Mỗi khối được XOR với khối đã mã hóa trước đó
- **CTR** (Counter): Chuyển đổi mã hóa khối thành mã hóa dòng
- **GCM** (Galois/Counter Mode): Cung cấp cả mã hóa và xác thực

### Cách sử dụng chế độ văn bản:
1. Chọn "Text Mode" (chế độ mặc định)
2. Chọn thuật toán từ danh sách thả xuống
3. Chọn kích thước khóa (bit) phù hợp với thuật toán
4. Chọn chế độ hoạt động (Mode) và padding
5. Nhập hoặc tạo khóa bằng nút "Generate Key"
6. Nếu chế độ yêu cầu IV (Initialization Vector), hãy nhập hoặc tạo IV bằng nút "Generate IV"
7. Nhập văn bản cần mã hóa vào ô "Input Text"
8. Nhấn nút "Encrypt" để mã hóa hoặc "Decrypt" để giải mã
9. Kết quả sẽ hiển thị trong ô "Output Text"

### Cách sử dụng chế độ tệp:
1. Chọn "File Mode"
2. Chọn thuật toán, kích thước khóa, chế độ và padding như trong chế độ văn bản
3. Nhập hoặc tạo khóa và IV (nếu cần)
4. Nhấn "Browse..." bên cạnh "Input File" để chọn tệp cần mã hóa/giải mã
5. Nhấn "Browse..." bên cạnh "Output File" để chọn vị trí lưu tệp kết quả
6. Nhấn nút "Encrypt" để mã hóa hoặc "Decrypt" để giải mã
7. Một thông báo sẽ hiển thị khi quá trình hoàn tất

### Quản lý khóa:
- **Generate Key**: Tạo khóa ngẫu nhiên với kích thước đã chọn
- **Load Key**: Tải khóa từ tệp
- **Save Key**: Lưu khóa hiện tại vào tệp
- **Generate IV**: Tạo vector khởi tạo ngẫu nhiên (chỉ cần thiết cho một số chế độ)

## 3. Tab Mã hóa bất đối xứng (Asymmetric)

Tab này cung cấp mã hóa bất đối xứng sử dụng thuật toán RSA:

### Các tính năng:
- Tạo cặp khóa (công khai/riêng tư)
- Mã hóa bằng khóa công khai
- Giải mã bằng khóa riêng tư
- Xuất/nhập khóa từ tệp

### Cách sử dụng:
1. Chọn kích thước khóa (1024, 2048, 3072 hoặc 4096 bit)
2. Nhấn "Generate Key Pair" để tạo cặp khóa mới
3. Để mã hóa:
   - Nhập văn bản vào ô "Input Text"
   - Đảm bảo khóa công khai đã được tải
   - Nhấn "Encrypt"
4. Để giải mã:
   - Nhập văn bản đã mã hóa vào ô "Input Text"
   - Đảm bảo khóa riêng tư đã được tải
   - Nhấn "Decrypt"
5. Để lưu khóa:
   - Nhấn "Save Public Key" hoặc "Save Private Key"
   - Chọn vị trí lưu tệp
6. Để tải khóa:
   - Nhấn "Load Public Key" hoặc "Load Private Key"
   - Chọn tệp khóa để tải

## Xử lý sự cố

### Lỗi JNI
Nếu bạn gặp lỗi JNI khi chạy ứng dụng, hãy thử các giải pháp sau:

1. **Đảm bảo tệp java.policy tồn tại**: Kiểm tra xem tệp java.policy có trong cùng thư mục với tệp thực thi không. Nếu không, hãy tạo tệp với nội dung:
   ```
   // Cấp tất cả quyền cho ứng dụng
   grant {
       permission java.security.AllPermission;
   };
   ```

2. **Sử dụng tệp JAR thay vì EXE**: Thử chạy ứng dụng bằng tệp JAR thay vì tệp thực thi:
   ```
   java -jar target/enctool.jar -Djava.security.policy=java.policy
   ```

3. **Kiểm tra kiến trúc hệ thống**: Đảm bảo hệ thống của bạn là 64-bit, vì JRE được tích hợp được cấu hình cho hệ thống 64-bit.

### Lỗi khác
Nếu bạn gặp lỗi khác:

1. **Lỗi mã hóa/giải mã**: Đảm bảo bạn đang sử dụng đúng khóa và IV (nếu cần) cho quá trình giải mã.

2. **Lỗi định dạng khóa**: Đảm bảo bạn nhập khóa theo đúng định dạng yêu cầu cho thuật toán đã chọn.

3. **Lỗi tệp**: Đảm bảo bạn có quyền đọc/ghi đối với các tệp đầu vào và đầu ra.

## Thuật ngữ

- **Mã hóa (Encryption)**: Quá trình chuyển đổi thông tin thành mã để ngăn truy cập trái phép.
- **Giải mã (Decryption)**: Quá trình chuyển đổi thông tin đã mã hóa trở lại dạng ban đầu.
- **Khóa (Key)**: Một chuỗi bit được sử dụng bởi thuật toán mã hóa để xác định cách mã hóa dữ liệu.
- **IV (Initialization Vector)**: Một khối bit được sử dụng cùng với khóa để đảm bảo rằng các văn bản giống nhau không tạo ra cùng một văn bản mã hóa.
- **Mã hóa đối xứng**: Phương pháp mã hóa sử dụng cùng một khóa cho cả mã hóa và giải mã.
- **Mã hóa bất đối xứng**: Phương pháp mã hóa sử dụng một cặp khóa - khóa công khai để mã hóa và khóa riêng tư để giải mã.
- **Padding**: Kỹ thuật thêm dữ liệu vào văn bản để đạt được kích thước khối cần thiết cho thuật toán mã hóa.

## Lưu ý bảo mật

1. **Bảo vệ khóa**: Luôn giữ khóa của bạn an toàn, đặc biệt là khóa riêng tư trong mã hóa bất đối xứng.
2. **Chọn khóa mạnh**: Sử dụng khóa có độ dài lớn hơn khi có thể (ví dụ: AES-256 thay vì AES-128).
3. **Tránh ECB**: Chế độ ECB không được khuyến nghị cho hầu hết các ứng dụng vì nó không cung cấp tính bảo mật tốt.
4. **Sử dụng IV duy nhất**: Đối với các chế độ yêu cầu IV, hãy đảm bảo sử dụng IV duy nhất cho mỗi lần mã hóa.